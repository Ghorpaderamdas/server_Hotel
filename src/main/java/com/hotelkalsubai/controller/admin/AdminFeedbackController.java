package com.hotelkalsubai.controller.admin;

import com.hotelkalsubai.dto.ApiResponse;
import com.hotelkalsubai.entity.Feedback;
import com.hotelkalsubai.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin/feedback")
@Tag(name = "Admin - Feedback", description = "Admin feedback management APIs")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ADMIN')")
public class AdminFeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping
    @Operation(summary = "Get all feedback", description = "Retrieve all feedback including pending approval")
    public ResponseEntity<?> getAllFeedback() {
        List<Feedback> feedback = feedbackService.getAllFeedback();
        return ResponseEntity.ok(ApiResponse.success("Feedback retrieved successfully", feedback));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending feedback", description = "Retrieve feedback pending approval")
    public ResponseEntity<?> getPendingFeedback() {
        List<Feedback> feedback = feedbackService.getPendingFeedback();
        return ResponseEntity.ok(ApiResponse.success("Pending feedback retrieved successfully", feedback));
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "Approve feedback", description = "Approve a feedback/review")
    public ResponseEntity<?> approveFeedback(@PathVariable Long id) {
        try {
            Feedback approvedFeedback = feedbackService.approveFeedback(id);
            return ResponseEntity.ok(ApiResponse.success("Feedback approved successfully", approvedFeedback));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete feedback", description = "Delete a feedback/review")
    public ResponseEntity<?> deleteFeedback(@PathVariable Long id) {
        return feedbackService.getFeedbackById(id)
                .map(feedback -> {
                    feedbackService.deleteFeedback(id);
                    return ResponseEntity.ok(ApiResponse.success("Feedback deleted successfully"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}