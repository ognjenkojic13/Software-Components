package rs.ac.raf.sessionservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class GameRequest {

    @NotBlank
    private String name;

    private String description;

    private String genre;
}
