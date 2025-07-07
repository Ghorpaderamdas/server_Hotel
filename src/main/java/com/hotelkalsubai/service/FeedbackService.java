package com.hotelkalsubai.service;

import com.hotelkalsubai.entity.Feedback;
import com.hotelkalsubai.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public List<Feedback> getApprovedFeedback() {
        return feedbackRepository.findByIsApprovedTrueOrderBySubmittedAtDesc();
    }

    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    public List<Feedback> getPendingFeedback() {
        return feedbackRepository.findByIsApprovedFalseOrderBySubmittedAtDesc();
    }

    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }

    public Feedback approveFeedback(Long id) {
        Optional<Feedback> feedbackOpt = feedbackRepository.findById(id);
        if (feedbackOpt.isEmpty()) {
            throw new RuntimeException("Feedback not found");
        }

        Feedback feedback = feedbackOpt.get();
        feedback.setIsApproved(true);
        return feedbackRepository.save(feedback);
    }

    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    public Double getAverageRating() {
        Double average = feedbackRepository.findAverageRating();
        return average != null ? average : 0.0;
    }
}