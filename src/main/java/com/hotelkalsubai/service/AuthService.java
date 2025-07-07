package com.hotelkalsubai.service;

import com.hotelkalsubai.dto.auth.JwtResponse;
import com.hotelkalsubai.entity.User;
import com.hotelkalsubai.repository.UserRepository;
import com.hotelkalsubai.security.JwtUtils;
import com.hotelkalsubai.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    public void sendPasswordResetEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with email: " + email);
        }

        User user = userOpt.get();
        String resetToken = generateResetToken();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        String resetLink = "http://localhost:3000/reset-password?token=" + resetToken;
        emailService.sendPasswordResetEmail(email, resetLink);
    }

    public void resetPassword(String token, String newPassword) {
        Optional<User> userOpt = userRepository.findByResetToken(token);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid reset token");
        }

        User user = userOpt.get();
        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }

    public void sendMobileOtp(String phoneNumber) {
        Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with phone number: " + phoneNumber);
        }

        String otpCode = generateOtpCode();
        User user = userOpt.get();

        user.setOtpCode(otpCode);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);

        smsService.sendOtp(phoneNumber, otpCode);
    }

    public void verifyOtpForPasswordReset(String phoneNumber, String otpCode, String newPassword) {
        Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        if (user.getOtpCode() == null || !user.getOtpCode().equals(otpCode)) {
            throw new RuntimeException("Invalid OTP");
        }

        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setOtpCode(null);
        user.setOtpExpiry(null);
        userRepository.save(user);
    }

    public JwtResponse authenticateWithGoogle(String token) {
        GoogleUserInfo googleUser = verifyGoogleToken(token);
        Optional<User> userOpt = userRepository.findByGoogleId(googleUser.getId());

        User user = userOpt.orElseGet(() -> {
            User newUser = new User();
            newUser.setGoogleId(googleUser.getId());
            newUser.setEmail(googleUser.getEmail());
            newUser.setUsername(googleUser.getName());
            newUser.setIsEmailVerified(true);
            newUser.setRoles(Set.of(User.Role.ROLE_USER));
            return userRepository.save(newUser);
        });

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        String jwt = jwtUtils.generateJwtToken(authentication);

        List<String> roles = user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toList());

        return new JwtResponse(jwt, user.getId(), user.getUsername(), user.getEmail(), roles);
    }

    public JwtResponse authenticateWithFacebook(String token) {
        FacebookUserInfo facebookUser = verifyFacebookToken(token);
        Optional<User> userOpt = userRepository.findByFacebookId(facebookUser.getId());

        User user = userOpt.orElseGet(() -> {
            User newUser = new User();
            newUser.setFacebookId(facebookUser.getId());
            newUser.setEmail(facebookUser.getEmail());
            newUser.setUsername(facebookUser.getName());
            newUser.setIsEmailVerified(true);
            newUser.setRoles(Set.of(User.Role.ROLE_USER));
            return userRepository.save(newUser);
        });

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        String jwt = jwtUtils.generateJwtToken(authentication);

        List<String> roles = user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toList());

        return new JwtResponse(jwt, user.getId(), user.getUsername(), user.getEmail(), roles);
    }

    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    private String generateOtpCode() {
        return String.format("%06d", new Random().nextInt(1_000_000));
    }

    private GoogleUserInfo verifyGoogleToken(String token) {
        return new GoogleUserInfo("google_123", "user@gmail.com", "Google User");
    }

    private FacebookUserInfo verifyFacebookToken(String token) {
        return new FacebookUserInfo("fb_123", "user@facebook.com", "Facebook User");
    }

    // Helper classes
    private static class GoogleUserInfo {
        private final String id;
        private final String email;
        private final String name;

        public GoogleUserInfo(String id, String email, String name) {
            this.id = id;
            this.email = email;
            this.name = name;
        }

        public String getId() { return id; }
        public String getEmail() { return email; }
        public String getName() { return name; }
    }

    private static class FacebookUserInfo {
        private final String id;
        private final String email;
        private final String name;

        public FacebookUserInfo(String id, String email, String name) {
            this.id = id;
            this.email = email;
            this.name = name;
        }

        public String getId() { return id; }
        public String getEmail() { return email; }
        public String getName() { return name; }
    }
}
