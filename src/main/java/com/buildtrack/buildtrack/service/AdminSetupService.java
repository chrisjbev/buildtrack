package com.buildtrack.buildtrack.service;

import com.buildtrack.buildtrack.model.Role;
import com.buildtrack.buildtrack.model.User;
import com.buildtrack.buildtrack.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Makes sure the configured admin account exists on startup.
 *
 * <ul>
 *   <li>ADMIN_USERNAME names an existing user: that user is promoted to ADMIN.</li>
 *   <li>The user does not exist and ADMIN_PASSWORD is set: a new ADMIN user is created.</li>
 *   <li>Otherwise: a warning is logged and the app starts normally.</li>
 * </ul>
 */
@Configuration
public class AdminSetupService {

    private static final Logger log = LoggerFactory.getLogger(AdminSetupService.class);

    @Bean
    public CommandLineRunner createAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.username:}") String adminUsername,
            @Value("${app.admin.password:}") String adminPassword,
            @Value("${app.admin.email:}") String adminEmail) {

        return args -> {
            if (adminUsername == null || adminUsername.isBlank()) {
                return;
            }

            userRepository.findByUsername(adminUsername).ifPresentOrElse(
                    existing -> {
                        if (existing.getRole() != Role.ADMIN) {
                            existing.setRole(Role.ADMIN);
                            userRepository.save(existing);
                            log.info("Promoted user '{}' to ADMIN", adminUsername);
                        }
                    },
                    () -> {
                        if (adminPassword == null || adminPassword.isBlank()) {
                            log.warn("ADMIN_USERNAME '{}' does not exist and ADMIN_PASSWORD is not set; "
                                    + "no admin account was created", adminUsername);
                            return;
                        }
                        String email = (adminEmail == null || adminEmail.isBlank())
                                ? adminUsername + "@buildtrack.local"
                                : adminEmail;
                        userRepository.save(new User(
                                null,
                                adminUsername,
                                email,
                                passwordEncoder.encode(adminPassword),
                                Role.ADMIN));
                        log.info("Created ADMIN user '{}'", adminUsername);
                    });
        };
    }
}
