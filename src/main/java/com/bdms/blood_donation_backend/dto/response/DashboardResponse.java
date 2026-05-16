package com.bdms.blood_donation_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    // ── Common ─────────────────────────────────────────────
    private long totalOpenRequests;
    private long totalAvailableDonors;

    // ── SUPER_ADMIN + ADMIN ────────────────────────────────
    private long totalUsers;
    private long totalDonors;
    private long totalHospitals;
    private long totalRequests;
    private long totalFulfilledRequests;
    private long totalCancelledRequests;
    private long totalAppointments;
    private long totalCompletedAppointments;

    // ── HOSPITAL_STAFF ─────────────────────────────────────
    private long myOpenRequests;
    private long myTotalRequests;
    private long myUpcomingAppointments;
    private long myInventoryTypes;

    // ── DONOR ──────────────────────────────────────────────
    private long myTotalDonations;
    private long myUpcomingAppointmentsCount;
    private long openRequestsForMyBloodType;
}