package com.hotelkalsubai.service;

import org.springframework.stereotype.Service;

@Service
public class SmsService {

    public void sendOtp(String phoneNumber, String otpCode) {
        // In production, integrate with SMS provider like Twilio, AWS SNS, etc.
        // For now, just log the OTP
        System.out.println("Sending OTP " + otpCode + " to " + phoneNumber);
        
        // Example Twilio integration:
        // Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        // Message message = Message.creator(
        //     new PhoneNumber(phoneNumber),
        //     new PhoneNumber(FROM_NUMBER),
        //     "Your Hotel Kalsubai OTP is: " + otpCode
        // ).create();
    }
}