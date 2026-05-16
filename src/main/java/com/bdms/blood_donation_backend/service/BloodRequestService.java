package com.bdms.blood_donation_backend.service;

import com.bdms.blood_donation_backend.dto.request.BloodRequestRequest;
import com.bdms.blood_donation_backend.dto.response.BloodRequestResponse;
import com.bdms.blood_donation_backend.entity.BloodRequest;
import com.bdms.blood_donation_backend.entity.Hospital;
import com.bdms.blood_donation_backend.enums.BloodType;
import com.bdms.blood_donation_backend.enums.RequestStatus;
import com.bdms.blood_donation_backend.exceptions.BadRequestException;
import com.bdms.blood_donation_backend.exceptions.ResourceNotFoundException;
import com.bdms.blood_donation_backend.repository.BloodRequestRepository;
import com.bdms.blood_donation_backend.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BloodRequestService {

    private final BloodRequestRepository bloodRequestRepository;
    private final HospitalRepository hospitalRepository;

    // ── Create blood request ───────────────────────────────
    public BloodRequestResponse createRequest(Long userId,
                                              BloodRequestRequest request) {
        Hospital hospital = hospitalRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital profile not found"));

        BloodRequest bloodRequest = BloodRequest.builder()
                .hospital(hospital)
                .bloodType(request.getBloodType())
                .urgency(request.getUrgency())
                .unitsNeeded(request.getUnitsNeeded())
                .notes(request.getNotes())
                .status(RequestStatus.OPEN)
                .build();

        bloodRequestRepository.save(bloodRequest);
        return mapToResponse(bloodRequest);
    }

    // ── Get all open requests ──────────────────────────────
    public List<BloodRequestResponse> getOpenRequests() {
        return bloodRequestRepository.findByStatus(RequestStatus.OPEN)
                .stream().map(this::mapToResponse).toList();
    }

    // ── Get my hospital requests ───────────────────────────
    public List<BloodRequestResponse> getMyRequests(Long userId) {
        Hospital hospital = hospitalRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital profile not found"));

        return bloodRequestRepository.findByHospitalId(hospital.getId())
                .stream().map(this::mapToResponse).toList();
    }

    // ── Get request by ID ──────────────────────────────────
    public BloodRequestResponse getRequestById(Long requestId) {
        BloodRequest request = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Blood request not found"));
        return mapToResponse(request);
    }

    // ── Update request status ──────────────────────────────
    public BloodRequestResponse updateStatus(Long requestId, RequestStatus status) {
        BloodRequest request = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Blood request not found"));

        request.setStatus(status);
        if (status == RequestStatus.FULFILLED) {
            request.setFulfilledAt(LocalDateTime.now());
        }

        bloodRequestRepository.save(request);
        return mapToResponse(request);
    }

    // ── Search by blood type ───────────────────────────────
    public List<BloodRequestResponse> searchByBloodType(BloodType bloodType) {
        return bloodRequestRepository
                .findByBloodTypeAndStatus(bloodType, RequestStatus.OPEN)
                .stream().map(this::mapToResponse).toList();
    }

    // ── Cancel request ─────────────────────────────────────
    public BloodRequestResponse cancelRequest(Long userId, Long requestId) {
        Hospital hospital = hospitalRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found"));

        BloodRequest request = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Blood request not found"));

        if (!request.getHospital().getId().equals(hospital.getId())) {
            throw new BadRequestException("You can only cancel your own requests");
        }

        request.setStatus(RequestStatus.CANCELLED);
        bloodRequestRepository.save(request);
        return mapToResponse(request);
    }

    // ── Mapper ─────────────────────────────────────────────
    private BloodRequestResponse mapToResponse(BloodRequest r) {
        return BloodRequestResponse.builder()
                .id(r.getId())
                .hospitalId(r.getHospital().getId())
                .hospitalName(r.getHospital().getHospitalName())
                .hospitalCity(r.getHospital().getCity())
                .bloodType(r.getBloodType())
                .urgency(r.getUrgency())
                .unitsNeeded(r.getUnitsNeeded())
                .unitsFulfilled(r.getUnitsFulfilled())
                .status(r.getStatus())
                .notes(r.getNotes())
                .requestedAt(r.getRequestedAt())
                .fulfilledAt(r.getFulfilledAt())
                .build();
    }
}