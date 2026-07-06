package rs.ac.raf.userservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class AttendanceBatchRequest {

    @NotNull
    private Long organizerId;

    @NotEmpty
    private List<AttendeeEntry> attendees;

    @Getter
    @Setter
    public static class AttendeeEntry {
        @NotNull
        private Long userId;

        @NotNull
        private Boolean attended;
    }
}
