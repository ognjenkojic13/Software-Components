package rs.ac.raf.userservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean enabled = false;

    @Column(nullable = false)
    private boolean blocked = false;

    private String activationToken;
    private LocalDateTime activationTokenExpiry;

    private String resetPasswordToken;
    private LocalDateTime resetPasswordTokenExpiry;

    @Column(nullable = false)
    private int totalSessionsJoined = 0;

    @Column(nullable = false)
    private int sessionsAttended = 0;

    @Column(nullable = false)
    private int sessionsLeft = 0;

    @Column(nullable = false)
    private double attendancePercentage = 100.0;

    @Column(nullable = false)
    private int sessionsOrganized = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrganizerTitle organizerTitle = OrganizerTitle.NEMA_TITULE;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
