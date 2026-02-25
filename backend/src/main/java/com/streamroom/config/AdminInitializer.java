package com.streamroom.config;

import com.streamroom.entity.User;
import com.streamroom.enums.Role;
import com.streamroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final String adminUsername;
    private final String adminPassword;

    public AdminInitializer(
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder,
            @Value("${admin.username}") String adminUsername,
            @Value("${admin.password}") String adminPassword
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(String... args) {
        // Migrate any existing admin users that don't yet have a role set
        userRepository.findAll().stream()
                .filter(u -> Boolean.TRUE.equals(u.getIsAdmin()) && u.getRole() == null)
                .forEach(u -> {
                    u.setRole(Role.ADMIN);
                    userRepository.save(u);
                });

        if (userRepository.existsByIsAdminTrue()) {
            return;
        }

        User admin = new User();
        admin.setUsername(adminUsername);
        admin.setEmail(adminUsername + "@streamroom.local");
        admin.setDisplayName(adminUsername);
        admin.setPasswordHash(passwordEncoder.encode(adminPassword));
        admin.setIsAdmin(true);
        admin.setRole(Role.ADMIN);
        admin.setTagline("Cyberpunk vibes. No cap.");
        admin.setBio("Welcome to StreamRoom — a cyberpunk-themed streaming hub built for gamers and content lovers.");
        admin.setTwitchUsername(adminUsername);
        admin.setSchedule("[{\"days\":\"MON – WED\",\"time\":\"7 PM – 10 PM\"},{\"days\":\"FRI\",\"time\":\"8 PM – 12 AM\"},{\"days\":\"SAT\",\"time\":\"3 PM – 8 PM\"}]");
        admin.setSocials("[{\"name\":\"Twitch\",\"url\":\"https://twitch.tv/" + adminUsername + "\"},{\"name\":\"Discord\",\"url\":\"https://discord.gg/\"},{\"name\":\"Twitter\",\"url\":\"https://twitter.com/" + adminUsername + "\"}]");
        admin.setFollowerCount(1200);
        admin.setStreamCount(348);
        admin.setHoursStreamed(4800);

        userRepository.save(admin);
    }
}
