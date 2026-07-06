package rs.ac.raf.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.raf.userservice.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByActivationToken(String activationToken);
    Optional<User> findByResetPasswordToken(String resetPasswordToken);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
