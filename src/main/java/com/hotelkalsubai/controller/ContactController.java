package com.hotelkalsubai.controller;

import com.hotelkalsubai.dto.ApiResponse;
import com.hotelkalsubai.dto.contact.ContactRequest;
import com.hotelkalsubai.entity.ContactInfo;
import com.hotelkalsubai.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/contact")
@Tag(name = "Contact", description = "Contact management APIs")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping
    @Operation(summary = "Get contact information", description = "Retrieve hotel contact information")
    public ResponseEntity<?> getContactInfo() {
        return contactService.getContactInfo()
                .map(contact -> ResponseEntity.ok(ApiResponse.success("Contact info retrieved successfully", contact)))
                .orElse(ResponseEntity.ok(ApiResponse.success("No contact info found", null)));
    }

    @PostMapping
    @Operation(summary = "Submit contact form", description = "Submit a contact form message")
    public ResponseEntity<?> submitContactForm(@Valid @RequestBody ContactRequest contactRequest) {
        contactService.handleContactForm(
                contactRequest.getName(),
                contactRequest.getEmail(),
                contactRequest.getPhone(),
                contactRequest.getSubject(),
                contactRequest.getMessage()
        );
        return ResponseEntity.ok(ApiResponse.success("Contact form submitted successfully"));
    }
}