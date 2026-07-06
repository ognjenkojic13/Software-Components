package rs.ac.raf.sessionservice.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReminderScheduler {

    private final SessionService sessionService;

    public ReminderScheduler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Scheduled(fixedRate = 60000)
    public void sendUpcomingReminders() {
        sessionService.checkAndSendReminders();
    }
}
