package rs.ac.raf.sessionservice.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EligibilityResponse {
    private Long userId;
    private String username;
    private String email;
    private boolean blocked;
    private double attendancePercentage;
}
