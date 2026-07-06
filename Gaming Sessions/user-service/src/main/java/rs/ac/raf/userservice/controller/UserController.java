package rs.ac.raf.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rs.ac.raf.userservice.dto.AdminUserView;
import rs.ac.raf.userservice.dto.UpdateProfileRequest;
import rs.ac.raf.userservice.dto.UserProfileResponse;
import rs.ac.raf.userservice.security.GatewayPrincipal;
import rs.ac.raf.userservice.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile(@AuthenticationPrincipal GatewayPrincipal principal) {
        return ResponseEntity.ok(userService.getProfile(principal.getUserId()));
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateMyProfile(@AuthenticationPrincipal GatewayPrincipal principal,
                                                                @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateProfile(principal.getUserId(), request));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminUserView>> listAllUsers() {
        return ResponseEntity.ok(userService.listAllUsers());
    }

    @PutMapping("/admin/{id}/block")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> blockUser(@PathVariable Long id) {
        userService.blockUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin/{id}/unblock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> unblockUser(@PathVariable Long id) {
        userService.unblockUser(id);
        return ResponseEntity.noContent().build();
    }
}
