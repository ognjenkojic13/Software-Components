package rs.ac.raf.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationTypeResponse {
    private Long id;
    private String code;
    private String description;
    private String subjectTemplate;
    private String bodyTemplate;
}
