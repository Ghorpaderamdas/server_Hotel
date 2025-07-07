package com.hotelkalsubai.controller;

import com.hotelkalsubai.dto.ApiResponse;
import com.hotelkalsubai.entity.GalleryImage;
import com.hotelkalsubai.service.GalleryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/gallery")
@Tag(name = "Gallery", description = "Gallery management APIs")
public class GalleryController {

    @Autowired
    private GalleryService galleryService;

    @GetMapping
    @Operation(summary = "Get all gallery images", description = "Retrieve all gallery images")
    public ResponseEntity<?> getAllImages() {
        List<GalleryImage> images = galleryService.getAllImages();
        return ResponseEntity.ok(ApiResponse.success("Gallery images retrieved successfully", images));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get images by category", description = "Retrieve gallery images by category")
    public ResponseEntity<?> getImagesByCategory(@PathVariable String category) {
        List<GalleryImage> images = galleryService.getImagesByCategory(category);
        return ResponseEntity.ok(ApiResponse.success("Gallery images retrieved successfully", images));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get image by ID", description = "Retrieve a specific gallery image by its ID")
    public ResponseEntity<?> getImageById(@PathVariable Long id) {
        return galleryService.getImageById(id)
                .map(image -> ResponseEntity.ok(ApiResponse.success("Gallery image found", image)))
                .orElse(ResponseEntity.notFound().build());
    }
}