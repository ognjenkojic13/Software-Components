package rs.ac.raf.sessionservice.client;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AttendanceBatchRequest {
    private Long organizerId;
    private List<AttendeeEntry> attendees;

    @Getter
    @Setter
    public static class AttendeeEntry {
        private Long userId;
        private Boolean attended;

        public AttendeeEntry() {
        }

        public AttendeeEntry(Long userId, Boolean attended) {
            this.userId = userId;
            this.attended = attended;
        }
    }
}
