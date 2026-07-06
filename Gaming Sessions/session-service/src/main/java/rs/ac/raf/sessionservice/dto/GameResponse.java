package rs.ac.raf.sessionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameResponse {
    private Long id;
    private String name;
    private String description;
    private String genre;
}
