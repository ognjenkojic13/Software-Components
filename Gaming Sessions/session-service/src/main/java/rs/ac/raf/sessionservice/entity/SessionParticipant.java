package rs.ac.raf.sessionservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "session_participants", uniqueConstraints = @UniqueConstraint(columnNames = {"session_id", "user_id"}))
@Getter
@Setter
@NoArgsConstructor
public class SessionParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private GameSession session;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDateTime joinedAt = LocalDateTime.now();

    private Boolean attended;
}
