package rs.ac.raf.sessionservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SessionResponse {
    private Long id;
    private String title;
    private GameResponse game;
    private Long organizerId;
    private String organizerUsername;
    private int maxPlayers;
    private int currentPlayers;
    private String sessionType;
    private LocalDateTime startDateTime;
    private String description;
    private String status;
    private boolean joinedByCurrentUser;
}
