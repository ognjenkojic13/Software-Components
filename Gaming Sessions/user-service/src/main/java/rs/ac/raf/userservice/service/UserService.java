package rs.ac.raf.userservice.service;

import rs.ac.raf.userservice.dto.*;

import java.util.List;

public interface UserService {
    void register(RegisterRequest request);
    void activate(String token);
    LoginResponse login(LoginRequest request);
    void forgotPassword(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);

    UserProfileResponse getProfile(Long userId);
    UserProfileResponse updateProfile(Long userId, UpdateProfileRequest request);

    List<AdminUserView> listAllUsers();
    void blockUser(Long userId);
    void unblockUser(Long userId);

    EligibilityResponse getEligibility(Long userId);
    void markJoined(Long userId);
    void applyAttendanceBatch(AttendanceBatchRequest request);
}
