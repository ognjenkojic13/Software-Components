package rs.ac.raf.sessionservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class SessionDetailResponse {
    private Long id;
    private String title;
    private GameResponse game;
    private Long organizerId;
    private String organizerUsername;
    private int maxPlayers;
    private String sessionType;
    private LocalDateTime startDateTime;
    private String description;
    private String status;
    private List<ParticipantView> participants;
}
