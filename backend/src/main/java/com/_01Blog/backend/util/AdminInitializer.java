package com._01Blog.backend.util;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com._01Blog.backend.model.entity.User;
import com._01Blog.backend.model.enums.Role;
import com._01Blog.backend.model.repository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class AdminInitializer implements ApplicationRunner {
    private final UserRepository userRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    private final org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Fix Report table schema (Hibernate update might not handle dropping NOT NULL)
        try {
            jdbcTemplate.execute("ALTER TABLE reports ALTER COLUMN reported_user_id DROP NOT NULL");
            jdbcTemplate.execute("ALTER TABLE reports ALTER COLUMN reported_post_id DROP NOT NULL");
        } catch (Exception e) {
            // Ignore if already fixed or table doesn't exist yet (though it should)
            System.err.println("Schema fix warning: " + e.getMessage());
        }

        try {
            User user = userRepository.findByEmail("walid@gmail.com").orElse(null);
            // ... (rest of user logic remains same, just ensuring correct context)
            if (user == null) {
                user = new User();
                user.setUserName("walid");
                user.setFirstName("walid");
                user.setLastName("zouguagh");
                user.setEmail("walid@gmail.com");
                user.setBio("Admin");
                user.setRole(Role.ADMIN);
                user.setPassword(passwordEncoder.encode("123"));
                userRepository.save(user);
            } else {
                if (!passwordEncoder.matches("123", user.getPassword())) {
                    user.setPassword(passwordEncoder.encode("123"));
                    userRepository.save(user);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
