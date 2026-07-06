package rs.ac.raf.sessionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import rs.ac.raf.sessionservice.entity.GameSession;
import rs.ac.raf.sessionservice.entity.SessionStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<GameSession, Long>, JpaSpecificationExecutor<GameSession> {
    List<GameSession> findByStatusAndReminderSentFalseAndStartDateTimeBetween(
            SessionStatus status, LocalDateTime from, LocalDateTime to);
}
