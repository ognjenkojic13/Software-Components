package rs.ac.raf.sessionservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import rs.ac.raf.sessionservice.entity.Game;
import rs.ac.raf.sessionservice.entity.GameSession;
import rs.ac.raf.sessionservice.entity.SessionType;
import rs.ac.raf.sessionservice.repository.GameRepository;
import rs.ac.raf.sessionservice.repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class SessionSpecificationsTest {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private GameRepository gameRepository;

    private Game csgo;
    private Game lol;

    @BeforeEach
    void setUp() {
        csgo = new Game();
        csgo.setName("CS2");
        csgo.setGenre("FPS");
        csgo = gameRepository.save(csgo);

        lol = new Game();
        lol.setName("League of Legends");
        lol.setGenre("MOBA");
        lol = gameRepository.save(lol);

        sessionRepository.save(session(csgo, SessionType.OPEN, "Rangovana partija za pocetnike"));
        sessionRepository.save(session(csgo, SessionType.CLOSED, "Privatni trening sa timom"));
        sessionRepository.save(session(lol, SessionType.OPEN, "Casual normal partija"));
    }

    @Test
    void filtersByGameAndType() {
        Specification<GameSession> spec = Specification
                .where(SessionSpecifications.hasGame(csgo.getId()))
                .and(SessionSpecifications.hasType(SessionType.OPEN));

        List<GameSession> result = sessionRepository.findAll(spec);

        assertEquals(1, result.size());
        assertEquals("Rangovana partija za pocetnike", result.get(0).getTitle());
    }

    @Test
    void filtersByDescriptionKeyword() {
        Specification<GameSession> spec = SessionSpecifications.descriptionContains("trening");

        List<GameSession> result = sessionRepository.findAll(spec);

        assertEquals(1, result.size());
        assertEquals(csgo.getId(), result.get(0).getGame().getId());
    }

    private GameSession session(Game game, SessionType type, String description) {
        GameSession session = new GameSession();
        session.setTitle(description);
        session.setGame(game);
        session.setOrganizerId(1L);
        session.setOrganizerUsername("organizer");
        session.setMaxPlayers(5);
        session.setSessionType(type);
        session.setStartDateTime(LocalDateTime.now().plusDays(1));
        session.setDescription(description);
        return session;
    }
}
