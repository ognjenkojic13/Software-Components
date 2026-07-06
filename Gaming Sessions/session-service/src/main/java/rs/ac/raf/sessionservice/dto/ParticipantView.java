package rs.ac.raf.sessionservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ParticipantView {
    private Long userId;
    private String username;
    private LocalDateTime joinedAt;
    private Boolean attended;
}
