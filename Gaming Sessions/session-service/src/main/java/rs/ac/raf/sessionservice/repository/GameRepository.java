package rs.ac.raf.sessionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.raf.sessionservice.entity.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
    boolean existsByNameIgnoreCase(String name);
}
