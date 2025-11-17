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
    public UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // throw new UnsupportedOperationException("Unimplemented method 'run'");
        User user = new User();
        user.setUserName("walid");
        user.setFirstName("walid");
        user.setLastName("zouguagh");
        user.setEmail("walid@gmail.com");
        user.setPassword("123");
        user.setBio("Admin");
        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }
}
