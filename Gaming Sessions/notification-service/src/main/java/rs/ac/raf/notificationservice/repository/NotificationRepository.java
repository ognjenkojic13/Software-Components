package rs.ac.raf.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.raf.notificationservice.entity.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientUserIdOrderByCreatedAtDesc(Long recipientUserId);
    List<Notification> findAllByOrderByCreatedAtDesc();
}
