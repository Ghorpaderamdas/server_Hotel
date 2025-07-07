package com.hotelkalsubai.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
@EntityListeners(AuditingEntityListener.class)
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "guest_name", nullable = false)
    private String guestName;

    @Column(name = "guest_email")
    private String guestEmail;

    @NotNull
    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer rating;

    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment;

    @Column(name = "is_approved")
    private Boolean isApproved = false;

    @CreatedDate
    @Column(name = "submitted_at", nullable = false, updatable = false)
    private LocalDateTime submittedAt;

    // Constructors
    public Feedback() {}

    public Feedback(String guestName, String guestEmail, Integer rating, String comment) {
        this.guestName = guestName;
        this.guestEmail = guestEmail;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }

    public String getGuestEmail() { return guestEmail; }
    public void setGuestEmail(String guestEmail) { this.guestEmail = guestEmail; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Boolean getIsApproved() { return isApproved; }
    public void setIsApproved(Boolean isApproved) { this.isApproved = isApproved; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}