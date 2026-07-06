package rs.ac.raf.userservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminUserView {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private boolean enabled;
    private boolean blocked;
    private Double attendancePercentage;
    private String organizerTitle;
}
