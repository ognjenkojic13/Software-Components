package rs.ac.raf.notificationservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.raf.notificationservice.entity.Notification;
import rs.ac.raf.notificationservice.entity.NotificationStatus;
import rs.ac.raf.notificationservice.entity.NotificationType;
import rs.ac.raf.notificationservice.messaging.NotificationMessage;
import rs.ac.raf.notificationservice.repository.NotificationRepository;
import rs.ac.raf.notificationservice.repository.NotificationTypeRepository;

@Service
public class NotificationProcessingService {

    private final NotificationTypeRepository typeRepository;
    private final NotificationRepository notificationRepository;
    private final MailSenderService mailSenderService;

    public NotificationProcessingService(NotificationTypeRepository typeRepository,
                                          NotificationRepository notificationRepository,
                                          MailSenderService mailSenderService) {
        this.typeRepository = typeRepository;
        this.notificationRepository = notificationRepository;
        this.mailSenderService = mailSenderService;
    }

    @Transactional
    public void process(NotificationMessage message) {
        NotificationType type = typeRepository.findByCode(message.getNotificationType())
                .orElseThrow(() -> new IllegalArgumentException("Nepoznat tip notifikacije: " + message.getNotificationType()));

        String subject = TemplateRenderer.render(type.getSubjectTemplate(), message.getTemplateData());
        String body = TemplateRenderer.render(type.getBodyTemplate(), message.getTemplateData());

        boolean sent = mailSenderService.send(message.getRecipientEmail(), subject, body);

        Notification notification = new Notification();
        notification.setRecipientUserId(message.getRecipientUserId());
        notification.setRecipientEmail(message.getRecipientEmail());
        notification.setTypeCode(type.getCode());
        notification.setSubject(subject);
        notification.setBody(body);
        notification.setStatus(sent ? NotificationStatus.SENT : NotificationStatus.FAILED);
        notificationRepository.save(notification);
    }
}
