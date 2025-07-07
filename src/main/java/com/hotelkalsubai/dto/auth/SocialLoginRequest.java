package com.hotelkalsubai.dto.auth;

import jakarta.validation.constraints.NotBlank;

public class SocialLoginRequest {
    @NotBlank
    private String token; // Google/Facebook token

    @NotBlank
    private String provider; // "google" or "facebook"


    // Constructors
    public SocialLoginRequest() {}

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

}