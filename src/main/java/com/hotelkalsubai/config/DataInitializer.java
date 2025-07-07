package com.hotelkalsubai.config;

import com.hotelkalsubai.entity.User;
import com.hotelkalsubai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create default admin user if not exists
        if (!userRepository.existsByEmail("admin@hotelkalsubai.com")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@hotelkalsubai.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setPhoneNumber("+919876543210");
            admin.setRoles(Set.of(User.Role.ROLE_ADMIN, User.Role.ROLE_USER));
            admin.setIsEmailVerified(true);
            admin.setIsPhoneVerified(true);
            
            userRepository.save(admin);
            System.out.println("Default admin user created: admin@hotelkalsubai.com / admin123");
        }
    }
}