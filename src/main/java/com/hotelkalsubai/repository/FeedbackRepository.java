package com.hotelkalsubai.repository;

import com.hotelkalsubai.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByIsApprovedTrueOrderBySubmittedAtDesc();
    List<Feedback> findByIsApprovedFalseOrderBySubmittedAtDesc();
    
    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.isApproved = true")
    Double findAverageRating();
}