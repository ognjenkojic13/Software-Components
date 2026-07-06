package rs.ac.raf.notificationservice.messaging;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class NotificationMessage implements Serializable {
    private String notificationType;
    private Long recipientUserId;
    private String recipientEmail;
    private Map<String, String> templateData;
}
