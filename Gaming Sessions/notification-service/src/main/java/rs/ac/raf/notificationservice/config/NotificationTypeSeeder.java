package rs.ac.raf.notificationservice.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.ac.raf.notificationservice.entity.NotificationType;
import rs.ac.raf.notificationservice.repository.NotificationTypeRepository;

import java.util.Arrays;
import java.util.List;

@Component
public class NotificationTypeSeeder implements CommandLineRunner {

    private final NotificationTypeRepository repository;

    public NotificationTypeSeeder(NotificationTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        List<NotificationType> defaults = Arrays.asList(
                type("ACTIVATION_EMAIL", "Aktivacioni imejl",
                        "Aktivirajte Vas Gaming Sessions nalog",
                        "Zdravo {{username}},\n\nAktivirajte nalog klikom na link: http://localhost:3000/activate?token={{activationToken}}"),
                type("PASSWORD_RESET", "Reset lozinke",
                        "Reset lozinke - Gaming Sessions",
                        "Zdravo {{username}},\n\nResetujte lozinku klikom na link: http://localhost:3000/reset-password?token={{resetToken}}"),
                type("SESSION_INVITATION", "Pozivnica za sesiju",
                        "Pozvani ste na gejming sesiju",
                        "Zdravo,\n\nPozvani ste na sesiju '{{sessionTitle}}'. Prihvatite pozivnicu: http://localhost:3000/invitations/accept?sessionId={{sessionId}}&token={{invitationToken}}"),
                type("JOIN_CONFIRMATION", "Potvrda prijave",
                        "Uspesno ste se prijavili na sesiju",
                        "Zdravo {{username}},\n\nUspesno ste prijavljeni na sesiju '{{sessionTitle}}' koja pocinje {{startDateTime}}."),
                type("SESSION_CANCELLED", "Otkazivanje sesije",
                        "Sesija je otkazana",
                        "Zdravo {{username}},\n\nSesija '{{sessionTitle}}' je otkazana."),
                type("SESSION_REMINDER", "Podsetnik pre pocetka sesije",
                        "Podsetnik: sesija pocinje uskoro",
                        "Zdravo {{username}},\n\nSesija '{{sessionTitle}}' pocinje za 60 minuta ({{startDateTime}})."),
                type("SESSION_CREATION_REJECTED", "Odbijanje kreiranja sesije",
                        "Kreiranje sesije odbijeno",
                        "Zdravo {{username}},\n\nVas zahtev za kreiranje sesije je odbijen jer procenat prisustva nije bar 90% (trenutno: {{attendancePercentage}}%)."),
                type("ORGANIZER_TITLE_EARNED", "Nova organizatorska titula",
                        "Cestitamo na novoj tituli!",
                        "Zdravo {{username}},\n\nOsvojili ste novu organizatorsku titulu: {{newTitle}}.")
        );

        for (NotificationType candidate : defaults) {
            if (!repository.existsByCode(candidate.getCode())) {
                repository.save(candidate);
            }
        }
    }

    private NotificationType type(String code, String description, String subject, String body) {
        NotificationType type = new NotificationType();
        type.setCode(code);
        type.setDescription(description);
        type.setSubjectTemplate(subject);
        type.setBodyTemplate(body);
        return type;
    }
}
