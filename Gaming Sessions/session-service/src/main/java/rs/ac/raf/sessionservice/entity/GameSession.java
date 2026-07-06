package rs.ac.raf.sessionservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sessions")
@Getter
@Setter
@NoArgsConstructor
public class GameSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(optional = false)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(nullable = false)
    private Long organizerId;

    @Column(nullable = false)
    private String organizerUsername;

    @Column(nullable = false)
    private int maxPlayers;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionType sessionType;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status = SessionStatus.SCHEDULED;

    @Column(nullable = false)
    private boolean reminderSent = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionParticipant> participants = new ArrayList<>();
}
