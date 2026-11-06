package com.buildtrack.buildtrack.service;

import com.buildtrack.buildtrack.model.Role;
import com.buildtrack.buildtrack.model.User;
import com.buildtrack.buildtrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminSetupService {

    @Bean
    public CommandLineRunner createAdmin(
            UserRepository userRepository,
            @Value("${app.admin.username:}") String adminUsername) {

        return args -> {
            if (adminUsername == null || adminUsername.isBlank()) {
                return;
            }

            User admin = userRepository.findByUsername(adminUsername)
                    .orElseThrow(() ->
                            new IllegalStateException(
                                    "Configured admin user does not exist"));

            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        };
    }
}
