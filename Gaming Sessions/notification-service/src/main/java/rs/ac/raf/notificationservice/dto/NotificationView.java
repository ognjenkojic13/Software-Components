package rs.ac.raf.notificationservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationView {
    private Long id;
    private Long recipientUserId;
    private String recipientEmail;
    private String typeCode;
    private String subject;
    private String body;
    private String status;
    private LocalDateTime createdAt;
}
