package com.bdms.blood_donation_backend.service;

import com.bdms.blood_donation_backend.dto.response.DashboardResponse;
import com.bdms.blood_donation_backend.entity.DonorProfile;
import com.bdms.blood_donation_backend.entity.Hospital;
import com.bdms.blood_donation_backend.entity.User;
import com.bdms.blood_donation_backend.enums.AppointmentStatus;
import com.bdms.blood_donation_backend.enums.RequestStatus;
import com.bdms.blood_donation_backend.enums.UserRole;
import com.bdms.blood_donation_backend.exceptions.ResourceNotFoundException;
import com.bdms.blood_donation_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository              userRepository;
    private final DonorProfileRepository      donorProfileRepository;
    private final HospitalRepository          hospitalRepository;
    private final BloodRequestRepository      bloodRequestRepository;
    private final AppointmentRepository       appointmentRepository;
    private final InventoryRepository         inventoryRepository;

    public DashboardResponse getDashboard(User currentUser) {

        UserRole role = currentUser.getRole();

        return switch (role) {
            case SUPER_ADMIN -> buildSuperAdminDashboard();
            case ADMIN       -> buildAdminDashboard();
            case HOSPITAL_STAFF -> buildHospitalDashboard(currentUser.getId());
            case DONOR       -> buildDonorDashboard(currentUser.getId());
        };
    }

    // ── SUPER_ADMIN ────────────────────────────────────────
    private DashboardResponse buildSuperAdminDashboard() {
        return DashboardResponse.builder()
                .totalUsers(userRepository.count())
                .totalDonors(donorProfileRepository.count())
                .totalHospitals(hospitalRepository.count())
                .totalRequests(bloodRequestRepository.count())
                .totalOpenRequests(
                        bloodRequestRepository.countByStatus(RequestStatus.OPEN))
                .totalFulfilledRequests(
                        bloodRequestRepository.countByStatus(RequestStatus.FULFILLED))
                .totalCancelledRequests(
                        bloodRequestRepository.countByStatus(RequestStatus.CANCELLED))
                .totalAvailableDonors(
                        donorProfileRepository.countByIsAvailableTrue())
                .totalAppointments(appointmentRepository.count())
                .totalCompletedAppointments(
                        appointmentRepository.countByStatus(AppointmentStatus.COMPLETED))
                .build();
    }

    // ── ADMIN ──────────────────────────────────────────────
    private DashboardResponse buildAdminDashboard() {
        return DashboardResponse.builder()
                .totalOpenRequests(
                        bloodRequestRepository.countByStatus(RequestStatus.OPEN))
                .totalAvailableDonors(
                        donorProfileRepository.countByIsAvailableTrue())
                .totalDonors(donorProfileRepository.count())
                .totalHospitals(hospitalRepository.count())
                .totalRequests(bloodRequestRepository.count())
                .totalFulfilledRequests(
                        bloodRequestRepository.countByStatus(RequestStatus.FULFILLED))
                .totalAppointments(appointmentRepository.count())
                .totalCompletedAppointments(
                        appointmentRepository.countByStatus(AppointmentStatus.COMPLETED))
                .build();
    }

    // ── HOSPITAL_STAFF ─────────────────────────────────────
    private DashboardResponse buildHospitalDashboard(Long userId) {
        Hospital hospital = hospitalRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Hospital profile not found"));

        long hospitalId = hospital.getId();

        return DashboardResponse.builder()
                .myOpenRequests(
                        bloodRequestRepository.countByHospitalIdAndStatus(
                                hospitalId, RequestStatus.OPEN))
                .myTotalRequests(
                        bloodRequestRepository.countByHospitalId(hospitalId))
                .myUpcomingAppointments(
                        appointmentRepository.countByHospitalIdAndStatus(
                                hospitalId, AppointmentStatus.SCHEDULED))
                .myInventoryTypes(
                        inventoryRepository.countByHospitalId(hospitalId))
                .totalOpenRequests(
                        bloodRequestRepository.countByStatus(RequestStatus.OPEN))
                .totalAvailableDonors(
                        donorProfileRepository.countByIsAvailableTrue())
                .build();
    }

    // ── DONOR ──────────────────────────────────────────────
    private DashboardResponse buildDonorDashboard(Long userId) {
        DonorProfile donor = donorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Donor profile not found"));

        return DashboardResponse.builder()
                .myTotalDonations(donor.getTotalDonations())
                .myUpcomingAppointmentsCount(
                        appointmentRepository.countByDonorIdAndStatus(
                                donor.getId(), AppointmentStatus.SCHEDULED))
                .openRequestsForMyBloodType(
                        bloodRequestRepository.countByBloodTypeAndStatus(
                                donor.getBloodType(), RequestStatus.OPEN))
                .totalOpenRequests(
                        bloodRequestRepository.countByStatus(RequestStatus.OPEN))
                .build();
    }
}