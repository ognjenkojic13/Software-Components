package rs.ac.raf.notificationservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class NotificationTypeRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String description;

    @NotBlank
    private String subjectTemplate;

    @NotBlank
    private String bodyTemplate;
}
