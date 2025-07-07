package com.hotelkalsubai.controller.admin;

import com.hotelkalsubai.dto.ApiResponse;
import com.hotelkalsubai.entity.ContactInfo;
import com.hotelkalsubai.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin/contact")
@Tag(name = "Admin - Contact", description = "Admin contact management APIs")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ADMIN')")
public class AdminContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping
    @Operation(summary = "Update contact information", description = "Update hotel contact information")
    public ResponseEntity<?> updateContactInfo(@Valid @RequestBody ContactInfo contactInfo) {
        ContactInfo savedContactInfo = contactService.saveContactInfo(contactInfo);
        return ResponseEntity.ok(ApiResponse.success("Contact information updated successfully", savedContactInfo));
    }
}