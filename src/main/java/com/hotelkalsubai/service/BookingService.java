package com.hotelkalsubai.service;

import com.hotelkalsubai.entity.Booking;
import com.hotelkalsubai.entity.Room;
import com.hotelkalsubai.repository.BookingRepository;
import com.hotelkalsubai.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    public Booking createBooking(Long roomId, String guestName, String guestPhone, String guestEmail,
                                LocalDate checkInDate, LocalDate checkOutDate, String specialRequests) {
        Optional<Room> roomOpt = roomRepository.findById(roomId);
        if (roomOpt.isEmpty()) {
            throw new RuntimeException("Room not found");
        }

        Room room = roomOpt.get();
        
        // Check for conflicts
        List<Booking> conflicts = bookingRepository.findConflictingBookings(roomId, checkInDate, checkOutDate);
        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Room is not available for the selected dates");
        }

        // Calculate total amount
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        BigDecimal totalAmount = room.getPricePerNight().multiply(BigDecimal.valueOf(nights));

        Booking booking = new Booking(room, guestName, guestPhone, guestEmail, checkInDate, checkOutDate, totalAmount);
        booking.setSpecialRequests(specialRequests);

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getBookingsByEmail(String email) {
        return bookingRepository.findByGuestEmail(email);
    }

    public Booking updateBookingStatus(Long id, Booking.BookingStatus status) {
        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        if (bookingOpt.isEmpty()) {
            throw new RuntimeException("Booking not found");
        }

        Booking booking = bookingOpt.get();
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    public Booking updatePaymentStatus(Long id, Booking.PaymentStatus paymentStatus, String paymentId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        if (bookingOpt.isEmpty()) {
            throw new RuntimeException("Booking not found");
        }

        Booking booking = bookingOpt.get();
        booking.setPaymentStatus(paymentStatus);
        if (paymentId != null) {
            booking.setPaymentId(paymentId);
        }
        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByDateRange(LocalDate startDate, LocalDate endDate) {
        return bookingRepository.findBookingsByDateRange(startDate, endDate);
    }

    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        List<Booking> conflicts = bookingRepository.findConflictingBookings(roomId, checkIn, checkOut);
        return conflicts.isEmpty();
    }
}