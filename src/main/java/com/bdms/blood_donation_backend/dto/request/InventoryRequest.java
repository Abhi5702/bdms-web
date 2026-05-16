package com.bdms.blood_donation_backend.dto.request;

import com.bdms.blood_donation_backend.enums.BloodType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InventoryRequest {

    @NotNull(message = "Blood type is required")
    private BloodType bloodType;

    @Min(value = 0, message = "Units cannot be negative")
    private int unitsAvailable;

    private LocalDate expiryDate;
}