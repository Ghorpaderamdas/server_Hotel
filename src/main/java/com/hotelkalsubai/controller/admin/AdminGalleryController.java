package com.hotelkalsubai.controller.admin;

import com.hotelkalsubai.dto.ApiResponse;
import com.hotelkalsubai.entity.GalleryImage;
import com.hotelkalsubai.service.GalleryService;
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
@RequestMapping("/api/admin/gallery")
@Tag(name = "Admin - Gallery", description = "Admin gallery management APIs")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ADMIN')")
public class AdminGalleryController {

    @Autowired
    private GalleryService galleryService;

    @GetMapping
    @Operation(summary = "Get all gallery images (Admin)", description = "Retrieve all gallery images")
    public ResponseEntity<?> getAllImages() {
        List<GalleryImage> images = galleryService.getAllImages();
        return ResponseEntity.ok(ApiResponse.success("Gallery images retrieved successfully", images));
    }

    @PostMapping
    @Operation(summary = "Upload gallery image", description = "Upload a new gallery image")
    public ResponseEntity<?> uploadImage(@Valid @RequestBody GalleryImage galleryImage) {
        GalleryImage savedImage = galleryService.saveImage(galleryImage);
        return ResponseEntity.ok(ApiResponse.success("Gallery image uploaded successfully", savedImage));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update gallery image", description = "Update an existing gallery image")
    public ResponseEntity<?> updateImage(@PathVariable Long id, @Valid @RequestBody GalleryImage galleryImage) {
        return galleryService.getImageById(id)
                .map(existingImage -> {
                    galleryImage.setId(id);
                    GalleryImage updatedImage = galleryService.saveImage(galleryImage);
                    return ResponseEntity.ok(ApiResponse.success("Gallery image updated successfully", updatedImage));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete gallery image", description = "Delete a gallery image")
    public ResponseEntity<?> deleteImage(@PathVariable Long id) {
        return galleryService.getImageById(id)
                .map(image -> {
                    galleryService.deleteImage(id);
                    return ResponseEntity.ok(ApiResponse.success("Gallery image deleted successfully"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}