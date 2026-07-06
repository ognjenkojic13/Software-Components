package rs.ac.raf.sessionservice.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.raf.sessionservice.client.EligibilityResponse;
import rs.ac.raf.sessionservice.client.UserServiceClient;
import rs.ac.raf.sessionservice.dto.*;
import rs.ac.raf.sessionservice.entity.*;
import rs.ac.raf.sessionservice.exception.BadRequestException;
import rs.ac.raf.sessionservice.exception.ForbiddenException;
import rs.ac.raf.sessionservice.exception.NotFoundException;
import rs.ac.raf.sessionservice.messaging.NotificationEventPublisher;
import rs.ac.raf.sessionservice.messaging.NotificationMessage;
import rs.ac.raf.sessionservice.repository.InvitationRepository;
import rs.ac.raf.sessionservice.repository.SessionParticipantRepository;
import rs.ac.raf.sessionservice.repository.SessionRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SessionService {

    public static final double MIN_ATTENDANCE_TO_CREATE_SESSION = 90.0;
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final SessionRepository sessionRepository;
    private final SessionParticipantRepository participantRepository;
    private final InvitationRepository invitationRepository;
    private final GameService gameService;
    private final UserServiceClient userServiceClient;
    private final NotificationEventPublisher notificationEventPublisher;

    public SessionService(SessionRepository sessionRepository,
                           SessionParticipantRepository participantRepository,
                           InvitationRepository invitationRepository,
                           GameService gameService,
                           UserServiceClient userServiceClient,
                           NotificationEventPublisher notificationEventPublisher) {
        this.sessionRepository = sessionRepository;
        this.participantRepository = participantRepository;
        this.invitationRepository = invitationRepository;
        this.gameService = gameService;
        this.userServiceClient = userServiceClient;
        this.notificationEventPublisher = notificationEventPublisher;
    }

    @Transactional
    public SessionResponse createSession(SessionCreateRequest request, Long organizerId, String organizerUsername) {
        EligibilityResponse eligibility = userServiceClient.getEligibility(organizerId);

        if (eligibility.isBlocked()) {
            throw new ForbiddenException("Nalog je blokiran - kreiranje sesije nije dozvoljeno");
        }
        if (eligibility.getAttendancePercentage() < MIN_ATTENDANCE_TO_CREATE_SESSION) {
            publishCreationRejected(eligibility);
            throw new BadRequestException("Kreiranje sesije odbijeno: procenat prisustva ("
                    + eligibility.getAttendancePercentage() + "%) je ispod 90%");
        }

        Game game = gameService.getGameOrThrow(request.getGameId());

        GameSession session = new GameSession();
        session.setTitle(request.getTitle());
        session.setGame(game);
        session.setOrganizerId(organizerId);
        session.setOrganizerUsername(organizerUsername);
        session.setMaxPlayers(request.getMaxPlayers());
        session.setSessionType(request.getSessionType());
        session.setStartDateTime(request.getStartDateTime());
        session.setDescription(request.getDescription());
        session.setStatus(SessionStatus.SCHEDULED);

        session = sessionRepository.save(session);
        return toResponse(session, organizerId);
    }

    public List<SessionResponse> search(Long gameId, SessionType type, Integer maxPlayers, String description,
                                         boolean joinedOnly, String sortBy, Long currentUserId) {
        Specification<GameSession> spec = Specification.where(SessionSpecifications.hasGame(gameId))
                .and(SessionSpecifications.hasType(type))
                .and(SessionSpecifications.hasMaxPlayers(maxPlayers))
                .and(SessionSpecifications.descriptionContains(description))
                .and(joinedOnly ? SessionSpecifications.joinedByUser(currentUserId) : null);

        List<GameSession> sessions;
        if ("startTime".equalsIgnoreCase(sortBy)) {
            sessions = sessionRepository.findAll(spec, Sort.by(Sort.Direction.ASC, "startDateTime"));
        } else {
            sessions = sessionRepository.findAll(spec);
            if ("participants".equalsIgnoreCase(sortBy)) {
                sessions = sessions.stream()
                        .sorted(Comparator.comparingInt((GameSession s) -> s.getParticipants().size()).reversed())
                        .collect(Collectors.toList());
            }
        }

        return sessions.stream().map(s -> toResponse(s, currentUserId)).collect(Collectors.toList());
    }

    public SessionDetailResponse getDetail(Long sessionId) {
        GameSession session = getSessionOrThrow(sessionId);
        List<ParticipantView> participants = session.getParticipants().stream()
                .map(p -> ParticipantView.builder()
                        .userId(p.getUserId())
                        .username(p.getUsername())
                        .joinedAt(p.getJoinedAt())
                        .attended(p.getAttended())
                        .build())
                .collect(Collectors.toList());

        return SessionDetailResponse.builder()
                .id(session.getId())
                .title(session.getTitle())
                .game(new GameResponse(session.getGame().getId(), session.getGame().getName(),
                        session.getGame().getDescription(), session.getGame().getGenre()))
                .organizerId(session.getOrganizerId())
                .organizerUsername(session.getOrganizerUsername())
                .maxPlayers(session.getMaxPlayers())
                .sessionType(session.getSessionType().name())
                .startDateTime(session.getStartDateTime())
                .description(session.getDescription())
                .status(session.getStatus().name())
                .participants(participants)
                .build();
    }

    @Transactional
    public void join(Long sessionId, Long userId, String username, String invitationToken) {
        GameSession session = getSessionOrThrow(sessionId);

        if (session.getStatus() != SessionStatus.SCHEDULED) {
            throw new BadRequestException("Sesija nije aktivna");
        }
        if (participantRepository.existsBySessionIdAndUserId(sessionId, userId)) {
            throw new BadRequestException("Vec ste prijavljeni za ovu sesiju");
        }
        if (session.getParticipants().size() >= session.getMaxPlayers()) {
            throw new BadRequestException("Sesija je popunjena");
        }

        if (session.getSessionType() == SessionType.CLOSED) {
            Invitation invitation = invitationRepository.findByToken(invitationToken == null ? "" : invitationToken)
                    .orElseThrow(() -> new ForbiddenException("Zatvorena sesija zahteva vazecu pozivnicu"));
            if (invitation.isUsed() || !invitation.getSession().getId().equals(sessionId)) {
                throw new ForbiddenException("Pozivnica nije vazeca za ovu sesiju");
            }
            invitation.setUsed(true);
            invitationRepository.save(invitation);
        }

        EligibilityResponse eligibility = userServiceClient.getEligibility(userId);
        if (eligibility.isBlocked()) {
            throw new ForbiddenException("Nalog je blokiran");
        }

        SessionParticipant participant = new SessionParticipant();
        participant.setSession(session);
        participant.setUserId(userId);
        participant.setUsername(username);
        participant.setEmail(eligibility.getEmail());
        session.getParticipants().add(participant);
        sessionRepository.save(session);

        userServiceClient.markJoined(userId);

        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("sessionTitle", session.getTitle());
        data.put("startDateTime", session.getStartDateTime().format(DISPLAY_FORMAT));
        notificationEventPublisher.publish(NotificationMessage.builder()
                .notificationType("JOIN_CONFIRMATION")
                .recipientUserId(userId)
                .recipientEmail(eligibility.getEmail())
                .templateData(data)
                .build());
    }

    @Transactional
    public String invite(Long sessionId, Long requesterId, InviteRequest request) {
        GameSession session = getSessionOrThrow(sessionId);
        requireOrganizer(session, requesterId);

        Invitation invitation = new Invitation();
        invitation.setSession(session);
        invitation.setInvitedEmail(request.getEmail());
        invitation.setToken(UUID.randomUUID().toString());
        invitationRepository.save(invitation);

        Map<String, String> data = new HashMap<>();
        data.put("sessionTitle", session.getTitle());
        data.put("sessionId", String.valueOf(session.getId()));
        data.put("invitationToken", invitation.getToken());
        notificationEventPublisher.publish(NotificationMessage.builder()
                .notificationType("SESSION_INVITATION")
                .recipientEmail(request.getEmail())
                .templateData(data)
                .build());

        return invitation.getToken();
    }

    @Transactional
    public void cancel(Long sessionId, Long requesterId, boolean requesterIsAdmin) {
        GameSession session = getSessionOrThrow(sessionId);
        if (!requesterIsAdmin) {
            requireOrganizer(session, requesterId);
        }
        if (session.getStatus() != SessionStatus.SCHEDULED) {
            throw new BadRequestException("Samo zakazana sesija moze biti otkazana");
        }
        session.setStatus(SessionStatus.CANCELLED);
        sessionRepository.save(session);

        for (SessionParticipant participant : session.getParticipants()) {
            Map<String, String> data = new HashMap<>();
            data.put("username", participant.getUsername());
            data.put("sessionTitle", session.getTitle());
            notificationEventPublisher.publish(NotificationMessage.builder()
                    .notificationType("SESSION_CANCELLED")
                    .recipientUserId(participant.getUserId())
                    .recipientEmail(participant.getEmail())
                    .templateData(data)
                    .build());
        }
    }

    @Transactional
    public void conclude(Long sessionId, Long requesterId, ConcludeRequest request) {
        GameSession session = getSessionOrThrow(sessionId);
        requireOrganizer(session, requesterId);

        if (session.getStatus() != SessionStatus.SCHEDULED) {
            throw new BadRequestException("Samo zakazana sesija moze biti zakljucena");
        }

        Map<Long, Boolean> attendanceByUser = request.getAttendees().stream()
                .collect(Collectors.toMap(ConcludeRequest.AttendanceEntry::getUserId,
                        ConcludeRequest.AttendanceEntry::getAttended));

        boolean anyoneAttended = false;
        for (SessionParticipant participant : session.getParticipants()) {
            Boolean attended = attendanceByUser.get(participant.getUserId());
            if (attended == null) {
                attended = Boolean.FALSE;
            }
            participant.setAttended(attended);
            if (attended) {
                anyoneAttended = true;
            }
        }

        if (!anyoneAttended) {
            throw new BadRequestException(
                    "Sesija mora imati bar jednog igraca (pored organizatora) koji joj je prisustvovao");
        }

        session.setStatus(SessionStatus.FINISHED);
        sessionRepository.save(session);

        rs.ac.raf.sessionservice.client.AttendanceBatchRequest batchRequest =
                new rs.ac.raf.sessionservice.client.AttendanceBatchRequest();
        batchRequest.setOrganizerId(session.getOrganizerId());
        batchRequest.setAttendees(session.getParticipants().stream()
                .map(p -> new rs.ac.raf.sessionservice.client.AttendanceBatchRequest.AttendeeEntry(
                        p.getUserId(), p.getAttended()))
                .collect(Collectors.toList()));

        userServiceClient.applyAttendanceBatch(batchRequest);
    }

    @Transactional
    public void checkAndSendReminders() {
        LocalDateTime now = LocalDateTime.now();
        List<GameSession> upcoming = sessionRepository.findByStatusAndReminderSentFalseAndStartDateTimeBetween(
                SessionStatus.SCHEDULED, now, now.plusMinutes(60));

        for (GameSession session : upcoming) {
            for (SessionParticipant participant : session.getParticipants()) {
                Map<String, String> data = new HashMap<>();
                data.put("username", participant.getUsername());
                data.put("sessionTitle", session.getTitle());
                data.put("startDateTime", session.getStartDateTime().format(DISPLAY_FORMAT));
                notificationEventPublisher.publish(NotificationMessage.builder()
                        .notificationType("SESSION_REMINDER")
                        .recipientUserId(participant.getUserId())
                        .recipientEmail(participant.getEmail())
                        .templateData(data)
                        .build());
            }
            session.setReminderSent(true);
            sessionRepository.save(session);
        }
    }

    private void publishCreationRejected(EligibilityResponse eligibility) {
        Map<String, String> data = new HashMap<>();
        data.put("username", eligibility.getUsername());
        data.put("attendancePercentage", String.valueOf(eligibility.getAttendancePercentage()));
        notificationEventPublisher.publish(NotificationMessage.builder()
                .notificationType("SESSION_CREATION_REJECTED")
                .recipientUserId(eligibility.getUserId())
                .recipientEmail(eligibility.getEmail())
                .templateData(data)
                .build());
    }

    private void requireOrganizer(GameSession session, Long userId) {
        if (!session.getOrganizerId().equals(userId)) {
            throw new ForbiddenException("Samo organizator sesije moze izvesti ovu akciju");
        }
    }

    private GameSession getSessionOrThrow(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sesija ne postoji: " + id));
    }

    private SessionResponse toResponse(GameSession session, Long currentUserId) {
        boolean joined = currentUserId != null && session.getParticipants().stream()
                .anyMatch(p -> p.getUserId().equals(currentUserId));

        return SessionResponse.builder()
                .id(session.getId())
                .title(session.getTitle())
                .game(new GameResponse(session.getGame().getId(), session.getGame().getName(),
                        session.getGame().getDescription(), session.getGame().getGenre()))
                .organizerId(session.getOrganizerId())
                .organizerUsername(session.getOrganizerUsername())
                .maxPlayers(session.getMaxPlayers())
                .currentPlayers(session.getParticipants().size())
                .sessionType(session.getSessionType().name())
                .startDateTime(session.getStartDateTime())
                .description(session.getDescription())
                .status(session.getStatus().name())
                .joinedByCurrentUser(joined)
                .build();
    }
}
