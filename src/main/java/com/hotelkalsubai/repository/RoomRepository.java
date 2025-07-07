package com.hotelkalsubai.repository;

import com.hotelkalsubai.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByIsAvailableTrue();
    List<Room> findByType(String type);
    
    @Query("SELECT r FROM Room r WHERE r.isAvailable = true AND r.id NOT IN " +
           "(SELECT b.room.id FROM Booking b WHERE b.status != 'CANCELLED' AND " +
           "((b.checkInDate <= :checkOut AND b.checkOutDate >= :checkIn)))")
    List<Room> findAvailableRooms(@Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);
}