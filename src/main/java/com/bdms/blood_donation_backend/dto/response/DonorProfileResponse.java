package com.bdms.blood_donation_backend.dto.response;

import com.bdms.blood_donation_backend.enums.BloodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonorProfileResponse {
    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String profileImageUrl;
    private BloodType bloodType;
    private String city;
    private String phone;
    private boolean isAvailable;
    private LocalDate lastDonationDate;
    private int totalDonations;
    private LocalDateTime createdAt;
}