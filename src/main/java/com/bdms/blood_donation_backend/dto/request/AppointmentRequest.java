package com.bdms.blood_donation_backend.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentRequest {

    @NotNull(message = "Hospital ID is required")
    private Long hospitalId;

    // optional — null means walk-in donation
    private Long requestId;

    @NotNull(message = "Scheduled time is required")
    @Future(message = "Appointment must be in the future")
    private LocalDateTime scheduledAt;

    private String notes;
}