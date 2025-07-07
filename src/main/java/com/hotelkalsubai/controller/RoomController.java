package com.hotelkalsubai.controller;

import com.hotelkalsubai.dto.ApiResponse;
import com.hotelkalsubai.entity.Room;
import com.hotelkalsubai.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/rooms")
@Tag(name = "Rooms", description = "Room management APIs")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping
    @Operation(summary = "Get all available rooms", description = "Retrieve all available rooms")
    public ResponseEntity<?> getAllAvailableRooms() {
        List<Room> rooms = roomService.getAllAvailableRooms();
        return ResponseEntity.ok(ApiResponse.success("Rooms retrieved successfully", rooms));
    }

    @GetMapping("/available")
    @Operation(summary = "Get available rooms for dates", description = "Retrieve rooms available for specific check-in and check-out dates")
    public ResponseEntity<?> getAvailableRooms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {
        List<Room> rooms = roomService.getAvailableRooms(checkIn, checkOut);
        return ResponseEntity.ok(ApiResponse.success("Available rooms retrieved successfully", rooms));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get room by ID", description = "Retrieve a specific room by its ID")
    public ResponseEntity<?> getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id)
                .map(room -> ResponseEntity.ok(ApiResponse.success("Room found", room)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get rooms by type", description = "Retrieve rooms by type (standard, deluxe, suite)")
    public ResponseEntity<?> getRoomsByType(@PathVariable String type) {
        List<Room> rooms = roomService.getRoomsByType(type);
        return ResponseEntity.ok(ApiResponse.success("Rooms retrieved successfully", rooms));
    }
}