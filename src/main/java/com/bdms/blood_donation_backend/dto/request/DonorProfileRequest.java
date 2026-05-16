package com.bdms.blood_donation_backend.dto.request;

import com.bdms.blood_donation_backend.enums.BloodType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DonorProfileRequest {

    @NotNull(message = "Blood type is required")
    private BloodType bloodType;

    @NotBlank(message = "City is required")
    private String city;

    private String phone;

    private boolean isAvailable = true;

    private LocalDate lastDonationDate;
}