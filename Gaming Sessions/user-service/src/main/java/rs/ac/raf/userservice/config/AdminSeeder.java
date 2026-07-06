package rs.ac.raf.userservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.ac.raf.userservice.entity.Role;
import rs.ac.raf.userservice.entity.User;
import rs.ac.raf.userservice.repository.UserRepository;

import java.time.LocalDate;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminEmail;
    private final String adminPassword;

    public AdminSeeder(UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        @Value("${app.admin.email}") String adminEmail,
                        @Value("${app.admin.password}") String adminPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(String... args) {
        if (userRepository.existsByEmail(adminEmail)) {
            return;
        }
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        admin.setEmail(adminEmail);
        admin.setDateOfBirth(LocalDate.of(1990, 1, 1));
        admin.setRole(Role.ADMIN);
        admin.setEnabled(true);
        admin.setBlocked(false);
        userRepository.save(admin);
    }
}
