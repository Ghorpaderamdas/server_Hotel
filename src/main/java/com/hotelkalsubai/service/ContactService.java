package com.hotelkalsubai.service;

import com.hotelkalsubai.entity.ContactInfo;
import com.hotelkalsubai.repository.ContactInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    private ContactInfoRepository contactInfoRepository;

    public Optional<ContactInfo> getContactInfo() {
        List<ContactInfo> contacts = contactInfoRepository.findAll();
        return contacts.isEmpty() ? Optional.empty() : Optional.of(contacts.get(0));
    }

    public ContactInfo saveContactInfo(ContactInfo contactInfo) {
        return contactInfoRepository.save(contactInfo);
    }

    // Method to handle contact form submissions (could send email, save to database, etc.)
    public void handleContactForm(String name, String email, String phone, String subject, String message) {
        // Implementation for handling contact form
        // This could involve sending emails, saving to database, etc.
        System.out.println("Contact form received from: " + name + " (" + email + ")");
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);
    }
}