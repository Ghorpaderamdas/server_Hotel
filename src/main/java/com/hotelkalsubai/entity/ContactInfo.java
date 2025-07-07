package com.hotelkalsubai.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "contact_info")
@EntityListeners(AuditingEntityListener.class)
public class ContactInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String phone;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;

    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    private String address;

    @Column(name = "map_link")
    private String mapLink;

    @Column(name = "whatsapp_number")
    private String whatsappNumber;

    @Column(name = "opening_hours")
    private String openingHours;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public ContactInfo() {}

    public ContactInfo(String phone, String email, String address) {
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getMapLink() { return mapLink; }
    public void setMapLink(String mapLink) { this.mapLink = mapLink; }

    public String getWhatsappNumber() { return whatsappNumber; }
    public void setWhatsappNumber(String whatsappNumber) { this.whatsappNumber = whatsappNumber; }

    public String getOpeningHours() { return openingHours; }
    public void setOpeningHours(String openingHours) { this.openingHours = openingHours; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}