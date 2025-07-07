package com.hotelkalsubai.repository;

import com.hotelkalsubai.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByGuestEmail(String guestEmail);
    List<Booking> findByStatus(Booking.BookingStatus status);
    List<Booking> findByPaymentStatus(Booking.PaymentStatus paymentStatus);
    
    @Query("SELECT b FROM Booking b WHERE b.room.id = :roomId AND b.status != 'CANCELLED' AND " +
           "((b.checkInDate <= :checkOut AND b.checkOutDate >= :checkIn))")
    List<Booking> findConflictingBookings(@Param("roomId") Long roomId, 
                                         @Param("checkIn") LocalDate checkIn, 
                                         @Param("checkOut") LocalDate checkOut);
    
    @Query("SELECT b FROM Booking b WHERE b.checkInDate >= :startDate AND b.checkInDate <= :endDate")
    List<Booking> findBookingsByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}