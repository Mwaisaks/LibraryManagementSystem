package com.mwaisaka.Library.Management.System.Config;

import com.mwaisaka.Library.Management.System.enums.UserRole;
import com.mwaisaka.Library.Management.System.models.User;
import com.mwaisaka.Library.Management.System.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createAdminUser();
    }

    private void createAdminUser() {
        String adminEmail = "admin@library.com";

        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setName("System Administrator");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(UserRole.LIBRARIAN);
            admin.setEnabled(true);
            admin.setAccountNonExpired(true);
            admin.setAccountNonLocked(true);
            admin.setCredentialsNonExpired(true);

            userRepository.save(admin);
            log.info("Admin user created successfully with email: {}", adminEmail);
            log.info("Default password: admin123 (Please change after first login)");
        } else {
            log.info("Admin user already exists");
        }
    }
}