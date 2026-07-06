package rs.ac.raf.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.raf.userservice.dto.AttendanceBatchRequest;
import rs.ac.raf.userservice.dto.EligibilityResponse;
import rs.ac.raf.userservice.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class InternalUserController {

    private final UserService userService;

    @GetMapping("/{id}/eligibility")
    public ResponseEntity<EligibilityResponse> getEligibility(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getEligibility(id));
    }

    @PutMapping("/{id}/joined")
    public ResponseEntity<Void> markJoined(@PathVariable Long id) {
        userService.markJoined(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/attendance-batch")
    public ResponseEntity<Void> applyAttendanceBatch(@Valid @RequestBody AttendanceBatchRequest request) {
        userService.applyAttendanceBatch(request);
        return ResponseEntity.noContent().build();
    }
}
