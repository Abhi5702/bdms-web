package com.bdms.blood_donation_backend.service;

import com.bdms.blood_donation_backend.dto.request.AppointmentRequest;
import com.bdms.blood_donation_backend.dto.response.AppointmentResponse;
import com.bdms.blood_donation_backend.entity.Appointment;
import com.bdms.blood_donation_backend.entity.BloodRequest;
import com.bdms.blood_donation_backend.entity.DonorProfile;
import com.bdms.blood_donation_backend.entity.Hospital;
import com.bdms.blood_donation_backend.enums.AppointmentStatus;
import com.bdms.blood_donation_backend.exceptions.BadRequestException;
import com.bdms.blood_donation_backend.exceptions.ResourceNotFoundException;
import com.bdms.blood_donation_backend.repository.AppointmentRepository;
import com.bdms.blood_donation_backend.repository.BloodRequestRepository;
import com.bdms.blood_donation_backend.repository.DonorProfileRepository;
import com.bdms.blood_donation_backend.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DonorProfileRepository donorProfileRepository;
    private final HospitalRepository hospitalRepository;
    private final BloodRequestRepository bloodRequestRepository;

    // ── Book appointment ───────────────────────────────────
    public AppointmentResponse bookAppointment(Long userId,
                                               AppointmentRequest request) {

        DonorProfile donor = donorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Donor profile not found"));

        Hospital hospital = hospitalRepository.findById(request.getHospitalId())
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found"));

        BloodRequest bloodRequest = null;
        if (request.getRequestId() != null) {
            bloodRequest = bloodRequestRepository.findById(request.getRequestId())
                    .orElseThrow(() -> new ResourceNotFoundException("Blood request not found"));
        }

        Appointment appointment = Appointment.builder()
                .donor(donor)
                .hospital(hospital)
                .request(bloodRequest)
                .scheduledAt(request.getScheduledAt())
                .notes(request.getNotes())
                .status(AppointmentStatus.SCHEDULED)
                .build();

        appointmentRepository.save(appointment);
        return mapToResponse(appointment);
    }

    // ── Get my appointments (donor) ────────────────────────
    public List<AppointmentResponse> getMyAppointments(Long userId) {

        DonorProfile donor = donorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Donor profile not found"));

        return appointmentRepository.findByDonorId(donor.getId())
                .stream().map(this::mapToResponse).toList();
    }

    // ── Get hospital appointments ──────────────────────────
    public List<AppointmentResponse> getHospitalAppointments(Long userId) {

        Hospital hospital = hospitalRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found"));

        return appointmentRepository.findByHospitalId(hospital.getId())
                .stream().map(this::mapToResponse).toList();
    }

    // ── Get all appointments (admin) ───────────────────────
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream().map(this::mapToResponse).toList();
    }

    // ── Update appointment status ──────────────────────────
    public AppointmentResponse updateStatus(Long appointmentId,
                                            AppointmentStatus status) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        appointment.setStatus(status);
        appointmentRepository.save(appointment);
        return mapToResponse(appointment);
    }

    // ── Cancel appointment (donor) ─────────────────────────
    public AppointmentResponse cancelAppointment(Long userId, Long appointmentId) {

        DonorProfile donor = donorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found"));

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (!appointment.getDonor().getId().equals(donor.getId())) {
            throw new BadRequestException("You can only cancel your own appointments");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new BadRequestException("Cannot cancel a completed appointment");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
        return mapToResponse(appointment);
    }

    // ── Mapper ─────────────────────────────────────────────
    private AppointmentResponse mapToResponse(Appointment a) {
        return AppointmentResponse.builder()
                .id(a.getId())
                .donorId(a.getDonor().getId())
                .donorName(a.getDonor().getUser().getFullName())
                .donorBloodType(a.getDonor().getBloodType().name())
                .hospitalId(a.getHospital().getId())
                .hospitalName(a.getHospital().getHospitalName())
                .requestId(a.getRequest() != null ? a.getRequest().getId() : null)
                .scheduledAt(a.getScheduledAt())
                .status(a.getStatus())
                .notes(a.getNotes())
                .createdAt(a.getCreatedAt())
                .build();
    }
}