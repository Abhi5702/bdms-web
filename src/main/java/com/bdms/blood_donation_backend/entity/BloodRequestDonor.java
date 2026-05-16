package com.bdms.blood_donation_backend.entity;

import com.bdms.blood_donation_backend.enums.DonorResponseStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "blood_request_donor")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BloodRequestDonor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private BloodRequest request;

    @ManyToOne
    @JoinColumn(name = "donor_id", nullable = false)
    private DonorProfile donor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DonorResponseStatus status = DonorResponseStatus.PENDING;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}