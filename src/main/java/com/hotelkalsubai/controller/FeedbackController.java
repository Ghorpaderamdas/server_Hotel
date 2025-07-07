package com.hotelkalsubai.controller;

import com.hotelkalsubai.dto.ApiResponse;
import com.hotelkalsubai.entity.Feedback;
import com.hotelkalsubai.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/feedback")
@Tag(name = "Feedback", description = "Feedback management APIs")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping
    @Operation(summary = "Get approved feedback", description = "Retrieve all approved feedback/reviews")
    public ResponseEntity<?> getApprovedFeedback() {
        List<Feedback> feedback = feedbackService.getApprovedFeedback();
        return ResponseEntity.ok(ApiResponse.success("Feedback retrieved successfully", feedback));
    }

    @PostMapping
    @Operation(summary = "Submit feedback", description = "Submit new feedback/review")
    public ResponseEntity<?> submitFeedback(@Valid @RequestBody Feedback feedback) {
        Feedback savedFeedback = feedbackService.saveFeedback(feedback);
        return ResponseEntity.ok(ApiResponse.success("Feedback submitted successfully", savedFeedback));
    }

    @GetMapping("/average-rating")
    @Operation(summary = "Get average rating", description = "Get the average rating from all approved feedback")
    public ResponseEntity<?> getAverageRating() {
        Double averageRating = feedbackService.getAverageRating();
        return ResponseEntity.ok(ApiResponse.success("Average rating retrieved successfully", averageRating));
    }
}