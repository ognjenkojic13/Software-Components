package rs.ac.raf.userservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UserProfileResponse {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private String role;
    private boolean enabled;
    private boolean blocked;
    private Integer totalSessionsJoined;
    private Integer sessionsAttended;
    private Integer sessionsLeft;
    private Double attendancePercentage;
    private Integer sessionsOrganized;
    private String organizerTitle;
}
