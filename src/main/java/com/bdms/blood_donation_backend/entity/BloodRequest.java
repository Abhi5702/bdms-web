package com.bdms.blood_donation_backend.entity;

import com.bdms.blood_donation_backend.enums.BloodType;
import com.bdms.blood_donation_backend.enums.RequestStatus;
import com.bdms.blood_donation_backend.enums.UrgencyLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "blood_request")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BloodRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_type", nullable = false)
    private BloodType bloodType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UrgencyLevel urgency;

    @Column(name = "units_needed", nullable = false)
    private int unitsNeeded;

    @Column(name = "units_fulfilled", nullable = false)
    private int unitsFulfilled = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.OPEN;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "requested_at", updatable = false)
    private LocalDateTime requestedAt;

    @Column(name = "fulfilled_at")
    private LocalDateTime fulfilledAt;

    @PrePersist
    protected void onCreate() { requestedAt = LocalDateTime.now(); }
}