package com.hotelkalsubai.controller.admin;

import com.hotelkalsubai.dto.ApiResponse;
import com.hotelkalsubai.entity.Booking;
import com.hotelkalsubai.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin/bookings")
@Tag(name = "Admin - Bookings", description = "Admin booking management APIs")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    @Operation(summary = "Get all bookings", description = "Retrieve all bookings")
    public ResponseEntity<?> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(ApiResponse.success("Bookings retrieved successfully", bookings));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get bookings by date range", description = "Retrieve bookings within a specific date range")
    public ResponseEntity<?> getBookingsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Booking> bookings = bookingService.getBookingsByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("Bookings retrieved successfully", bookings));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update booking status", description = "Update the status of a booking")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            Booking.BookingStatus bookingStatus = Booking.BookingStatus.valueOf(status.toUpperCase());
            Booking updatedBooking = bookingService.updateBookingStatus(id, bookingStatus);
            return ResponseEntity.ok(ApiResponse.success("Booking status updated successfully", updatedBooking));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid status value"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/payment-status")
    @Operation(summary = "Update payment status", description = "Update the payment status of a booking")
    public ResponseEntity<?> updatePaymentStatus(@PathVariable Long id, 
                                                @RequestParam String paymentStatus,
                                                @RequestParam(required = false) String paymentId) {
        try {
            Booking.PaymentStatus status = Booking.PaymentStatus.valueOf(paymentStatus.toUpperCase());
            Booking updatedBooking = bookingService.updatePaymentStatus(id, status, paymentId);
            return ResponseEntity.ok(ApiResponse.success("Payment status updated successfully", updatedBooking));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid payment status value"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}