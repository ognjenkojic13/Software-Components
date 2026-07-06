package rs.ac.raf.userservice.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.raf.userservice.dto.*;
import rs.ac.raf.userservice.entity.OrganizerTitle;
import rs.ac.raf.userservice.entity.Role;
import rs.ac.raf.userservice.entity.User;
import rs.ac.raf.userservice.exception.BadRequestException;
import rs.ac.raf.userservice.exception.ConflictException;
import rs.ac.raf.userservice.exception.NotFoundException;
import rs.ac.raf.userservice.messaging.NotificationEventPublisher;
import rs.ac.raf.userservice.messaging.NotificationMessage;
import rs.ac.raf.userservice.repository.UserRepository;
import rs.ac.raf.userservice.security.JwtService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final NotificationEventPublisher notificationEventPublisher;

    public UserServiceImpl(UserRepository userRepository,
                            PasswordEncoder passwordEncoder,
                            JwtService jwtService,
                            NotificationEventPublisher notificationEventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.notificationEventPublisher = notificationEventPublisher;
    }

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Nalog sa ovim imejlom vec postoji");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Korisnicko ime je zauzeto");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setRole(Role.PLAYER);
        user.setEnabled(false);
        user.setActivationToken(UUID.randomUUID().toString());
        user.setActivationTokenExpiry(LocalDateTime.now().plusHours(24));
        userRepository.save(user);

        Map<String, String> data = new HashMap<>();
        data.put("username", user.getUsername());
        data.put("activationToken", user.getActivationToken());
        notificationEventPublisher.publish(NotificationMessage.builder()
                .notificationType("ACTIVATION_EMAIL")
                .recipientUserId(user.getId())
                .recipientEmail(user.getEmail())
                .templateData(data)
                .build());
    }

    @Override
    @Transactional
    public void activate(String token) {
        User user = userRepository.findByActivationToken(token)
                .orElseThrow(() -> new BadRequestException("Nevazeci aktivacioni token"));
        if (user.getActivationTokenExpiry() == null || user.getActivationTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Aktivacioni token je istekao");
        }
        user.setEnabled(true);
        user.setActivationToken(null);
        user.setActivationTokenExpiry(null);
        userRepository.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Pogresan imejl ili lozinka"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Pogresan imejl ili lozinka");
        }
        if (!user.isEnabled()) {
            throw new BadRequestException("Nalog nije aktiviran");
        }
        if (user.isBlocked()) {
            throw new BadRequestException("Nalog je blokiran");
        }

        String token = jwtService.generateToken(user);
        return new LoginResponse(token, user.getId(), user.getUsername(), user.getRole().name());
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null) {
            return; // ne otkrivamo da li email postoji
        }
        user.setResetPasswordToken(UUID.randomUUID().toString());
        user.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        Map<String, String> data = new HashMap<>();
        data.put("username", user.getUsername());
        data.put("resetToken", user.getResetPasswordToken());
        notificationEventPublisher.publish(NotificationMessage.builder()
                .notificationType("PASSWORD_RESET")
                .recipientUserId(user.getId())
                .recipientEmail(user.getEmail())
                .templateData(data)
                .build());
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByResetPasswordToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Nevazeci token za reset lozinke"));
        if (user.getResetPasswordTokenExpiry() == null || user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token za reset lozinke je istekao");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiry(null);
        userRepository.save(user);
    }

    @Override
    public UserProfileResponse getProfile(Long userId) {
        return toProfileResponse(getUserOrThrow(userId));
    }

    @Override
    @Transactional
    public UserProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = getUserOrThrow(userId);
        if (!user.getEmail().equalsIgnoreCase(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Nalog sa ovim imejlom vec postoji");
        }
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setDateOfBirth(request.getDateOfBirth());
        userRepository.save(user);
        return toProfileResponse(user);
    }

    @Override
    public List<AdminUserView> listAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toAdminView)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void blockUser(Long userId) {
        User user = getUserOrThrow(userId);
        user.setBlocked(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void unblockUser(Long userId) {
        User user = getUserOrThrow(userId);
        user.setBlocked(false);
        userRepository.save(user);
    }

    @Override
    public EligibilityResponse getEligibility(Long userId) {
        User user = getUserOrThrow(userId);
        return new EligibilityResponse(user.getId(), user.getUsername(), user.getEmail(),
                user.isBlocked(), user.getAttendancePercentage());
    }

    @Override
    @Transactional
    public void markJoined(Long userId) {
        User user = getUserOrThrow(userId);
        user.setTotalSessionsJoined(user.getTotalSessionsJoined() + 1);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void applyAttendanceBatch(AttendanceBatchRequest request) {
        for (AttendanceBatchRequest.AttendeeEntry entry : request.getAttendees()) {
            User user = getUserOrThrow(entry.getUserId());
            if (Boolean.TRUE.equals(entry.getAttended())) {
                user.setSessionsAttended(user.getSessionsAttended() + 1);
            } else {
                user.setSessionsLeft(user.getSessionsLeft() + 1);
            }
            user.setAttendancePercentage(
                    AttendanceCalculator.calculatePercentage(user.getSessionsAttended(), user.getSessionsLeft()));
            userRepository.save(user);
        }

        User organizer = getUserOrThrow(request.getOrganizerId());
        organizer.setSessionsOrganized(organizer.getSessionsOrganized() + 1);
        OrganizerTitle oldTitle = organizer.getOrganizerTitle();
        OrganizerTitle newTitle = OrganizerTitle.fromSessionsOrganized(organizer.getSessionsOrganized());
        organizer.setOrganizerTitle(newTitle);
        userRepository.save(organizer);

        if (newTitle != oldTitle) {
            Map<String, String> data = new HashMap<>();
            data.put("username", organizer.getUsername());
            data.put("newTitle", newTitle.name());
            notificationEventPublisher.publish(NotificationMessage.builder()
                    .notificationType("ORGANIZER_TITLE_EARNED")
                    .recipientUserId(organizer.getId())
                    .recipientEmail(organizer.getEmail())
                    .templateData(data)
                    .build());
        }
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Korisnik ne postoji: " + userId));
    }

    private UserProfileResponse toProfileResponse(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .role(user.getRole().name())
                .enabled(user.isEnabled())
                .blocked(user.isBlocked())
                .totalSessionsJoined(user.getTotalSessionsJoined())
                .sessionsAttended(user.getSessionsAttended())
                .sessionsLeft(user.getSessionsLeft())
                .attendancePercentage(user.getAttendancePercentage())
                .sessionsOrganized(user.getSessionsOrganized())
                .organizerTitle(user.getOrganizerTitle().name())
                .build();
    }

    private AdminUserView toAdminView(User user) {
        return AdminUserView.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .enabled(user.isEnabled())
                .blocked(user.isBlocked())
                .attendancePercentage(user.getAttendancePercentage())
                .organizerTitle(user.getOrganizerTitle().name())
                .build();
    }
}
