package com.hotelkalsubai.controller.admin;

import com.hotelkalsubai.dto.ApiResponse;
import com.hotelkalsubai.dto.auth.SignupRequest;
import com.hotelkalsubai.entity.User;
import com.hotelkalsubai.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin/users")
@Tag(name = "Admin - Users", description = "Admin user management APIs")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve all users")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    @PostMapping("/create-admin")
    @Operation(summary = "Create admin user", description = "Create a new admin user (Admin only)")
    public ResponseEntity<?> createAdminUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Email is already in use!"));
        }

        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Username is already taken!"));
        }

        // Create admin user with both ADMIN and USER roles
        Set<User.Role> roles = Set.of(User.Role.ROLE_ADMIN, User.Role.ROLE_USER);

        User user = userService.createUser(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword(),
                signUpRequest.getPhoneNumber(),
                roles);

        return ResponseEntity.ok(ApiResponse.success("Admin user created successfully!", user));
    }

    @PutMapping("/{id}/role")
    @Operation(summary = "Update user role", description = "Update user role (Admin only)")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestParam String role) {
        try {
            User.Role userRole = User.Role.valueOf(role.toUpperCase());
            User updatedUser = userService.updateUserRole(id, userRole);
            return ResponseEntity.ok(ApiResponse.success("User role updated successfully", updatedUser));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid role value"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Delete a user (Admin only)")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}