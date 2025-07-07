package com.hotelkalsubai.controller;

import com.hotelkalsubai.dto.ApiResponse;
import com.hotelkalsubai.entity.MenuItem;
import com.hotelkalsubai.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/menu")
@Tag(name = "Menu", description = "Menu management APIs")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping
    @Operation(summary = "Get all menu items", description = "Retrieve all available menu items")
    public ResponseEntity<?> getAllMenuItems() {
        List<MenuItem> menuItems = menuService.getAllMenuItems();
        return ResponseEntity.ok(ApiResponse.success("Menu items retrieved successfully", menuItems));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get menu items by category", description = "Retrieve menu items by category (veg, nonveg, extras)")
    public ResponseEntity<?> getMenuItemsByCategory(@PathVariable String category) {
        List<MenuItem> menuItems = menuService.getMenuItemsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success("Menu items retrieved successfully", menuItems));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get menu item by ID", description = "Retrieve a specific menu item by its ID")
    public ResponseEntity<?> getMenuItemById(@PathVariable Long id) {
        return menuService.getMenuItemById(id)
                .map(menuItem -> ResponseEntity.ok(ApiResponse.success("Menu item found", menuItem)))
                .orElse(ResponseEntity.notFound().build());
    }
}