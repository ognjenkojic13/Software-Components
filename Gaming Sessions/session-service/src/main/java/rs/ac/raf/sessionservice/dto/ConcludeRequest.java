package rs.ac.raf.sessionservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ConcludeRequest {

    @NotEmpty
    private List<AttendanceEntry> attendees;

    @Getter
    @Setter
    public static class AttendanceEntry {
        @NotNull
        private Long userId;

        @NotNull
        private Boolean attended;
    }
}
