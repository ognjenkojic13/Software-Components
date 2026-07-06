package rs.ac.raf.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EligibilityResponse {
    private Long userId;
    private String username;
    private String email;
    private boolean blocked;
    private double attendancePercentage;
}
