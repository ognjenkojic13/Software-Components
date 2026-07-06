package rs.ac.raf.notificationservice.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import rs.ac.raf.notificationservice.service.NotificationProcessingService;

@Component
public class NotificationMessageListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationMessageListener.class);
    public static final String NOTIFICATIONS_QUEUE = "notifications.queue";

    private final NotificationProcessingService processingService;

    public NotificationMessageListener(NotificationProcessingService processingService) {
        this.processingService = processingService;
    }

    @JmsListener(destination = NOTIFICATIONS_QUEUE)
    public void onMessage(NotificationMessage message) {
        try {
            processingService.process(message);
        } catch (Exception ex) {
            log.error("Obrada notifikacije tipa {} nije uspela: {}", message.getNotificationType(), ex.getMessage(), ex);
        }
    }
}
