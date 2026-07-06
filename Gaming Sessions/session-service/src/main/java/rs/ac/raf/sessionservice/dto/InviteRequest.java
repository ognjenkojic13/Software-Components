package rs.ac.raf.sessionservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class InviteRequest {

    @NotBlank
    @Email
    private String email;
}
