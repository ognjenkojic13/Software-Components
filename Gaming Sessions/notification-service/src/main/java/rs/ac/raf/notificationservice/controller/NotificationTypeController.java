package rs.ac.raf.notificationservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.raf.notificationservice.dto.NotificationTypeRequest;
import rs.ac.raf.notificationservice.dto.NotificationTypeResponse;
import rs.ac.raf.notificationservice.service.NotificationTypeService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/notification-types")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class NotificationTypeController {

    private final NotificationTypeService notificationTypeService;

    @GetMapping
    public ResponseEntity<List<NotificationTypeResponse>> listAll() {
        return ResponseEntity.ok(notificationTypeService.listAll());
    }

    @PostMapping
    public ResponseEntity<NotificationTypeResponse> create(@Valid @RequestBody NotificationTypeRequest request) {
        return ResponseEntity.ok(notificationTypeService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationTypeResponse> update(@PathVariable Long id,
                                                            @Valid @RequestBody NotificationTypeRequest request) {
        return ResponseEntity.ok(notificationTypeService.update(id, request));
    }
}
