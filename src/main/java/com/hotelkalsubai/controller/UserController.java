package com.hotelkalsubai.controller;

import com.hotelkalsubai.dto.ApiResponse;
import com.hotelkalsubai.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "User management APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get user profile", description = "Get current user's profile information")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        Map<String, Object> profile = new HashMap<>();
        profile.put("id", userDetails.getId());
        profile.put("username", userDetails.getUsername());
        profile.put("email", userDetails.getEmail());
        profile.put("roles", userDetails.getAuthorities());

        return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully", profile));
    }
}