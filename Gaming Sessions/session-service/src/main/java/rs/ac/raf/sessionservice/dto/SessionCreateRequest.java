package rs.ac.raf.sessionservice.dto;

import lombok.Getter;
import lombok.Setter;
import rs.ac.raf.sessionservice.entity.SessionType;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class SessionCreateRequest {

    @NotBlank
    private String title;

    @NotNull
    private Long gameId;

    @Min(1)
    private int maxPlayers;

    @NotNull
    private SessionType sessionType;

    @NotNull
    @Future
    private LocalDateTime startDateTime;

    private String description;
}
