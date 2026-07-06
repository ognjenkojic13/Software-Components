package rs.ac.raf.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.raf.notificationservice.entity.NotificationType;

import java.util.Optional;

public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {
    Optional<NotificationType> findByCode(String code);
    boolean existsByCode(String code);
}
