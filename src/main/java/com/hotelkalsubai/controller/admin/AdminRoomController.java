package com.hotelkalsubai.controller.admin;

import com.hotelkalsubai.dto.ApiResponse;
import com.hotelkalsubai.entity.Room;
import com.hotelkalsubai.service.RoomService;
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
@RequestMapping("/api/admin/rooms")
@Tag(name = "Admin - Rooms", description = "Admin room management APIs")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping
    @Operation(summary = "Get all rooms (Admin)", description = "Retrieve all rooms including unavailable ones")
    public ResponseEntity<?> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(ApiResponse.success("Rooms retrieved successfully", rooms));
    }

    @PostMapping
    @Operation(summary = "Create room", description = "Create a new room")
    public ResponseEntity<?> createRoom(@Valid @RequestBody Room room) {
        Room savedRoom = roomService.saveRoom(room);
        return ResponseEntity.ok(ApiResponse.success("Room created successfully", savedRoom));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update room", description = "Update an existing room")
    public ResponseEntity<?> updateRoom(@PathVariable Long id, @Valid @RequestBody Room room) {
        return roomService.getRoomById(id)
                .map(existingRoom -> {
                    room.setId(id);
                    Room updatedRoom = roomService.saveRoom(room);
                    return ResponseEntity.ok(ApiResponse.success("Room updated successfully", updatedRoom));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete room", description = "Delete a room")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        return roomService.getRoomById(id)
                .map(room -> {
                    roomService.deleteRoom(id);
                    return ResponseEntity.ok(ApiResponse.success("Room deleted successfully"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}