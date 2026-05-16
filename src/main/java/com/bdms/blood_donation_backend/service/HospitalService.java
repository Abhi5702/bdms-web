package com.bdms.blood_donation_backend.service;

import com.bdms.blood_donation_backend.dto.request.HospitalRequest;
import com.bdms.blood_donation_backend.dto.response.HospitalResponse;
import com.bdms.blood_donation_backend.entity.Hospital;
import com.bdms.blood_donation_backend.entity.User;
import com.bdms.blood_donation_backend.exceptions.BadRequestException;
import com.bdms.blood_donation_backend.exceptions.ResourceNotFoundException;
import com.bdms.blood_donation_backend.repository.HospitalRepository;
import com.bdms.blood_donation_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final UserRepository userRepository;

    // ── Create hospital profile ────────────────────────────
    public HospitalResponse createProfile(Long userId, HospitalRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (hospitalRepository.findByUserId(userId).isPresent()) {
            throw new BadRequestException("Hospital profile already exists");
        }

        Hospital hospital = Hospital.builder()
                .user(user)
                .hospitalName(request.getHospitalName())
                .city(request.getCity())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();

        hospitalRepository.save(hospital);
        return mapToResponse(hospital);
    }

    // ── Get my hospital profile ────────────────────────────
    public HospitalResponse getMyProfile(Long userId) {
        Hospital hospital = hospitalRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital profile not found"));
        return mapToResponse(hospital);
    }

    // ── Update hospital profile ────────────────────────────
    public HospitalResponse updateProfile(Long userId, HospitalRequest request) {
        Hospital hospital = hospitalRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital profile not found"));

        hospital.setHospitalName(request.getHospitalName());
        hospital.setCity(request.getCity());
        hospital.setPhone(request.getPhone());
        hospital.setAddress(request.getAddress());

        hospitalRepository.save(hospital);
        return mapToResponse(hospital);
    }

    // ── Get all hospitals ──────────────────────────────────
    public List<HospitalResponse> getAllHospitals() {
        return hospitalRepository.findAll()
                .stream().map(this::mapToResponse).toList();
    }

    // ── Mapper ─────────────────────────────────────────────
    private HospitalResponse mapToResponse(Hospital hospital) {
        return HospitalResponse.builder()
                .id(hospital.getId())
                .userId(hospital.getUser().getId())
                .hospitalName(hospital.getHospitalName())
                .city(hospital.getCity())
                .phone(hospital.getPhone())
                .address(hospital.getAddress())
                .createdAt(hospital.getCreatedAt())
                .build();
    }
}