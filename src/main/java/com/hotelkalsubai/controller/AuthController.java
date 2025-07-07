package com.hotelkalsubai.controller;

import com.hotelkalsubai.dto.ApiResponse;
import com.hotelkalsubai.dto.auth.*;
import com.hotelkalsubai.entity.User;
import com.hotelkalsubai.security.JwtUtils;
import com.hotelkalsubai.security.UserDetailsImpl;
import com.hotelkalsubai.service.AuthService;
import com.hotelkalsubai.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user with email and password")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            JwtResponse jwtResponse = new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles);

            return ResponseEntity.ok(ApiResponse.success("Login successful", jwtResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid credentials"));
        }
    }

    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new user account")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Email is already in use!"));
        }

        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Username is already taken!"));
        }

        // Only allow USER role registration via public signup
        Set<User.Role> roles = Set.of(User.Role.ROLE_USER);

        // Create new user account
        User user = userService.createUser(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword(),
                signUpRequest.getPhoneNumber(),
                roles);

        return ResponseEntity.ok(ApiResponse.success("User registered successfully!"));
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Reset password using reset token")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            authService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok(ApiResponse.success("Password reset successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password", description = "Send password reset email")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
            authService.sendPasswordResetEmail(request.getEmail());
            return ResponseEntity.ok(ApiResponse.success("Password reset email sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to send password reset email"));
        }
    }

    @PostMapping("/request-otp")
    @Operation(summary = "Request OTP", description = "Send OTP to registered phone number")
    public ResponseEntity<?> requestOtp(@Valid @RequestBody OtpRequest request) {
        try {
            authService.sendMobileOtp(request.getPhoneNumber());
            return ResponseEntity.ok(ApiResponse.success("OTP sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP", description = "Verify OTP and allow password reset")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        try {
            authService.verifyOtpForPasswordReset(request.getPhoneNumber(), request.getOtpCode(), request.getNewPassword());
            return ResponseEntity.ok(ApiResponse.success("Password reset successfully using OTP"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/google")
    @Operation(summary = "Google login", description = "Authenticate with Google")
    public ResponseEntity<?> googleLogin(@Valid @RequestBody SocialLoginRequest request) {
        try {
            JwtResponse jwtResponse = authService.authenticateWithGoogle(request.getToken());
            return ResponseEntity.ok(ApiResponse.success("Google login successful", jwtResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Google authentication failed"));
        }
    }

    @PostMapping("/facebook")
    @Operation(summary = "Facebook login", description = "Authenticate with Facebook")
    public ResponseEntity<?> facebookLogin(@Valid @RequestBody SocialLoginRequest request) {
        try {
            JwtResponse jwtResponse = authService.authenticateWithFacebook(request.getToken());
            return ResponseEntity.ok(ApiResponse.success("Facebook login successful", jwtResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Facebook authentication failed"));
        }
    }
}