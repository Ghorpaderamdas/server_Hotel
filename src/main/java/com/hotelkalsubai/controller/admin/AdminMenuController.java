package com.hotelkalsubai.controller.admin;

import com.hotelkalsubai.dto.ApiResponse;
import com.hotelkalsubai.entity.MenuItem;
import com.hotelkalsubai.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin/menu")
@Tag(name = "Admin - Menu", description = "Admin menu management APIs")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ADMIN')")
public class AdminMenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping
    @Operation(summary = "Get all menu items (Admin)", description = "Retrieve all menu items including unavailable ones")
    public ResponseEntity<?> getAllMenuItems() {
        List<MenuItem> menuItems = menuService.getAllMenuItemsForAdmin();
        return ResponseEntity.ok(ApiResponse.success("Menu items retrieved successfully", menuItems));
    }

    @PostMapping
    @Operation(summary = "Create menu item", description = "Create a new menu item")
    public ResponseEntity<?> createMenuItem(@Valid @RequestBody MenuItem menuItem) {
        MenuItem savedMenuItem = menuService.saveMenuItem(menuItem);
        return ResponseEntity.ok(ApiResponse.success("Menu item created successfully", savedMenuItem));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update menu item", description = "Update an existing menu item")
    public ResponseEntity<?> updateMenuItem(@PathVariable Long id, @Valid @RequestBody MenuItem menuItem) {
        return menuService.getMenuItemById(id)
                .map(existingItem -> {
                    menuItem.setId(id);
                    MenuItem updatedMenuItem = menuService.saveMenuItem(menuItem);
                    return ResponseEntity.ok(ApiResponse.success("Menu item updated successfully", updatedMenuItem));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete menu item", description = "Delete a menu item")
    public ResponseEntity<?> deleteMenuItem(@PathVariable Long id) {
        return menuService.getMenuItemById(id)
                .map(menuItem -> {
                    menuService.deleteMenuItem(id);
                    return ResponseEntity.ok(ApiResponse.success("Menu item deleted successfully"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}