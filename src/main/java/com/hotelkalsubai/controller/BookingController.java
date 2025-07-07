package com.hotelkalsubai.controller;

import com.hotelkalsubai.dto.ApiResponse;
import com.hotelkalsubai.dto.booking.BookingRequest;
import com.hotelkalsubai.entity.Booking;
import com.hotelkalsubai.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/booking")
@Tag(name = "Booking", description = "Booking management APIs")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    @Operation(summary = "Create a new booking", description = "Create a new room booking")
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        try {
            Booking booking = bookingService.createBooking(
                    bookingRequest.getRoomId(),
                    bookingRequest.getGuestName(),
                    bookingRequest.getGuestPhone(),
                    bookingRequest.getGuestEmail(),
                    bookingRequest.getCheckInDate(),
                    bookingRequest.getCheckOutDate(),
                    bookingRequest.getSpecialRequests()
            );
            return ResponseEntity.ok(ApiResponse.success("Booking created successfully", booking));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/guest/{email}")
    @Operation(summary = "Get bookings by guest email", description = "Retrieve all bookings for a specific guest email")
    public ResponseEntity<?> getBookingsByEmail(@PathVariable String email) {
        List<Booking> bookings = bookingService.getBookingsByEmail(email);
        return ResponseEntity.ok(ApiResponse.success("Bookings retrieved successfully", bookings));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get booking by ID", description = "Retrieve a specific booking by its ID")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .map(booking -> ResponseEntity.ok(ApiResponse.success("Booking found", booking)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/check-availability")
    @Operation(summary = "Check room availability", description = "Check if a room is available for specific dates")
    public ResponseEntity<?> checkAvailability(
            @RequestParam Long roomId,
            @RequestParam String checkIn,
            @RequestParam String checkOut) {
        try {
            boolean available = bookingService.isRoomAvailable(roomId, 
                java.time.LocalDate.parse(checkIn), 
                java.time.LocalDate.parse(checkOut));
            return ResponseEntity.ok(ApiResponse.success("Availability checked", available));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid date format"));
        }
    }
}