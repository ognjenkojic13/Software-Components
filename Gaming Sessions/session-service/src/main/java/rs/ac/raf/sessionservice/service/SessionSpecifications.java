package rs.ac.raf.sessionservice.service;

import org.springframework.data.jpa.domain.Specification;
import rs.ac.raf.sessionservice.entity.GameSession;
import rs.ac.raf.sessionservice.entity.SessionType;

public final class SessionSpecifications {

    private SessionSpecifications() {
    }

    public static Specification<GameSession> hasGame(Long gameId) {
        return (root, query, cb) -> gameId == null ? null : cb.equal(root.get("game").get("id"), gameId);
    }

    public static Specification<GameSession> hasType(SessionType type) {
        return (root, query, cb) -> type == null ? null : cb.equal(root.get("sessionType"), type);
    }

    public static Specification<GameSession> hasMaxPlayers(Integer maxPlayers) {
        return (root, query, cb) -> maxPlayers == null ? null : cb.equal(root.get("maxPlayers"), maxPlayers);
    }

    public static Specification<GameSession> descriptionContains(String keyword) {
        return (root, query, cb) -> (keyword == null || keyword.isBlank())
                ? null
                : cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%");
    }

    public static Specification<GameSession> joinedByUser(Long userId) {
        return (root, query, cb) -> {
            if (userId == null) {
                return null;
            }
            query.distinct(true);
            var join = root.join("participants");
            return cb.equal(join.<Long>get("userId"), userId);
        };
    }
}
