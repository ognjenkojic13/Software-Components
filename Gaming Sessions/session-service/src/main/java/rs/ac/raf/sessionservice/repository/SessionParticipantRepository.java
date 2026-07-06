package rs.ac.raf.sessionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.raf.sessionservice.entity.SessionParticipant;

import java.util.List;
import java.util.Optional;

public interface SessionParticipantRepository extends JpaRepository<SessionParticipant, Long> {
    Optional<SessionParticipant> findBySessionIdAndUserId(Long sessionId, Long userId);
    List<SessionParticipant> findBySessionId(Long sessionId);
    int countBySessionId(Long sessionId);
    boolean existsBySessionIdAndUserId(Long sessionId, Long userId);
}
