package rs.ac.raf.notificationservice.service;

import org.springframework.stereotype.Service;
import rs.ac.raf.notificationservice.dto.NotificationView;
import rs.ac.raf.notificationservice.entity.Notification;
import rs.ac.raf.notificationservice.repository.NotificationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationQueryService {

    private final NotificationRepository notificationRepository;

    public NotificationQueryService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<NotificationView> listForAdmin() {
        return notificationRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toView)
                .collect(Collectors.toList());
    }

    public List<NotificationView> listForPlayer(Long userId) {
        return notificationRepository.findByRecipientUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toView)
                .collect(Collectors.toList());
    }

    private NotificationView toView(Notification notification) {
        return NotificationView.builder()
                .id(notification.getId())
                .recipientUserId(notification.getRecipientUserId())
                .recipientEmail(notification.getRecipientEmail())
                .typeCode(notification.getTypeCode())
                .subject(notification.getSubject())
                .body(notification.getBody())
                .status(notification.getStatus().name())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
