package com.bdms.blood_donation_backend.dto.request;

import com.bdms.blood_donation_backend.enums.BloodType;
import com.bdms.blood_donation_backend.enums.UrgencyLevel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BloodRequestRequest {

    @NotNull(message = "Blood type is required")
    private BloodType bloodType;

    @NotNull(message = "Urgency level is required")
    private UrgencyLevel urgency;

    @Min(value = 1, message = "Units needed must be at least 1")
    private int unitsNeeded;

    private String notes;
}