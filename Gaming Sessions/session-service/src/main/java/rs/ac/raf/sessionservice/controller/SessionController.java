package rs.ac.raf.sessionservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.ac.raf.sessionservice.dto.*;
import rs.ac.raf.sessionservice.entity.SessionType;
import rs.ac.raf.sessionservice.security.GatewayPrincipal;
import rs.ac.raf.sessionservice.service.SessionService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping
    public ResponseEntity<SessionResponse> create(@AuthenticationPrincipal GatewayPrincipal principal,
                                                   @Valid @RequestBody SessionCreateRequest request) {
        return ResponseEntity.ok(sessionService.createSession(request, principal.getUserId(), principal.getUsername()));
    }

    @GetMapping
    public ResponseEntity<List<SessionResponse>> search(@AuthenticationPrincipal GatewayPrincipal principal,
                                                         @RequestParam(required = false) Long gameId,
                                                         @RequestParam(required = false) SessionType sessionType,
                                                         @RequestParam(required = false) Integer maxPlayers,
                                                         @RequestParam(required = false) String description,
                                                         @RequestParam(defaultValue = "false") boolean joinedOnly,
                                                         @RequestParam(required = false) String sortBy) {
        return ResponseEntity.ok(sessionService.search(gameId, sessionType, maxPlayers, description,
                joinedOnly, sortBy, principal.getUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionDetailResponse> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.getDetail(id));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<Void> join(@AuthenticationPrincipal GatewayPrincipal principal,
                                      @PathVariable Long id,
                                      @RequestParam(required = false) String invitationToken) {
        sessionService.join(id, principal.getUserId(), principal.getUsername(), invitationToken);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<Map<String, String>> invite(@AuthenticationPrincipal GatewayPrincipal principal,
                                                       @PathVariable Long id,
                                                       @Valid @RequestBody InviteRequest request) {
        String token = sessionService.invite(id, principal.getUserId(), request);
        Map<String, String> body = new HashMap<>();
        body.put("invitationToken", token);
        return ResponseEntity.ok(body);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@AuthenticationPrincipal GatewayPrincipal principal, @PathVariable Long id) {
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        sessionService.cancel(id, principal.getUserId(), isAdmin);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/conclude")
    public ResponseEntity<Void> conclude(@AuthenticationPrincipal GatewayPrincipal principal,
                                         @PathVariable Long id,
                                         @Valid @RequestBody ConcludeRequest request) {
        sessionService.conclude(id, principal.getUserId(), request);
        return ResponseEntity.noContent().build();
    }
}
