package rs.ac.raf.userservice.messaging;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventPublisher {

    public static final String NOTIFICATIONS_QUEUE = "notifications.queue";

    private final JmsTemplate jmsTemplate;

    public NotificationEventPublisher(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void publish(NotificationMessage message) {
        jmsTemplate.convertAndSend(NOTIFICATIONS_QUEUE, message);
    }
}
