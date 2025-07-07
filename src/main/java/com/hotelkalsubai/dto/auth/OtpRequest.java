package com.hotelkalsubai.dto.auth;

import jakarta.validation.constraints.NotBlank;

public class OtpRequest {
    @NotBlank
    private String phoneNumber;

    private String otpCode; // For verification

    // Constructors
    public OtpRequest() {}

    public OtpRequest(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }
}