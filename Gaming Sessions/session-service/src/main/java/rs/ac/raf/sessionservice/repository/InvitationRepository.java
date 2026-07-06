package rs.ac.raf.sessionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.raf.sessionservice.entity.Invitation;

import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Optional<Invitation> findByToken(String token);
}
