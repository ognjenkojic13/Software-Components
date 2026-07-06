package rs.ac.raf.notificationservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "notification_types")
@Getter
@Setter
@NoArgsConstructor
public class NotificationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, length = 500)
    private String subjectTemplate;

    @Column(nullable = false, length = 4000)
    private String bodyTemplate;
}
