package rs.ac.raf.notificationservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.raf.notificationservice.dto.NotificationView;
import rs.ac.raf.notificationservice.security.GatewayPrincipal;
import rs.ac.raf.notificationservice.service.NotificationQueryService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationQueryService notificationQueryService;

    @GetMapping
    public ResponseEntity<List<NotificationView>> list(@AuthenticationPrincipal GatewayPrincipal principal) {
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return ResponseEntity.ok(notificationQueryService.listForAdmin());
        }
        return ResponseEntity.ok(notificationQueryService.listForPlayer(principal.getUserId()));
    }
}
