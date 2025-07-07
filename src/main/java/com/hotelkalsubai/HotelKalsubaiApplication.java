package com.hotelkalsubai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HotelKalsubaiApplication {
    public static void main(String[] args) {
        SpringApplication.run(HotelKalsubaiApplication.class, args);
    }
}