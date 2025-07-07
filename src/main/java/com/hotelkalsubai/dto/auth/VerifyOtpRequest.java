package com.hotelkalsubai.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class VerifyOtpRequest {
    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String otpCode;

    @NotBlank
    @Size(min = 6, max = 40)
    private String newPassword;

    // Constructors
    public VerifyOtpRequest() {}

    public VerifyOtpRequest(String phoneNumber, String otpCode, String newPassword) {
        this.phoneNumber = phoneNumber;
        this.otpCode = otpCode;
        this.newPassword = newPassword;
    }

    // Getters and Setters
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}