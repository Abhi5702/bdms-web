package com.bdms.blood_donation_backend.service;

import com.bdms.blood_donation_backend.dto.request.DonorProfileRequest;
import com.bdms.blood_donation_backend.dto.response.DonorProfileResponse;
import com.bdms.blood_donation_backend.entity.DonorProfile;
import com.bdms.blood_donation_backend.entity.User;
import com.bdms.blood_donation_backend.enums.BloodType;
import com.bdms.blood_donation_backend.exceptions.BadRequestException;
import com.bdms.blood_donation_backend.exceptions.ResourceNotFoundException;
import com.bdms.blood_donation_backend.repository.DonorProfileRepository;
import com.bdms.blood_donation_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DonorService {

    private final DonorProfileRepository donorProfileRepository;
    private final UserRepository userRepository;

    // ── Create donor profile ───────────────────────────────
    public DonorProfileResponse createProfile(Long userId,
                                              DonorProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (donorProfileRepository.findByUserId(userId).isPresent()) {
            throw new BadRequestException("Donor profile already exists for this user");
        }

        DonorProfile profile = DonorProfile.builder()
                .user(user)
                .bloodType(request.getBloodType())
                .city(request.getCity())
                .phone(request.getPhone())
                .isAvailable(request.isAvailable())
                .lastDonationDate(request.getLastDonationDate())
                .build();

        donorProfileRepository.save(profile);
        return mapToResponse(profile);
    }

    // ── Get my profile ─────────────────────────────────────
    public DonorProfileResponse getMyProfile(Long userId) {
        DonorProfile profile = donorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Donor profile not found"));
        return mapToResponse(profile);
    }

    // ── Update donor profile ───────────────────────────────
    public DonorProfileResponse updateProfile(Long userId,
                                              DonorProfileRequest request) {
        DonorProfile profile = donorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Donor profile not found"));

        profile.setBloodType(request.getBloodType());
        profile.setCity(request.getCity());
        profile.setPhone(request.getPhone());
        profile.setAvailable(request.isAvailable());
        profile.setLastDonationDate(request.getLastDonationDate());

        donorProfileRepository.save(profile);
        return mapToResponse(profile);
    }

    // ── Search donors ──────────────────────────────────────
    public List<DonorProfileResponse> searchDonors(BloodType bloodType,
                                                   String city) {
        List<DonorProfile> donors;

        if (bloodType != null && city != null) {
            donors = donorProfileRepository
                    .findByBloodTypeAndCityAndIsAvailableTrue(bloodType, city);
        } else if (bloodType != null) {
            donors = donorProfileRepository
                    .findByBloodTypeAndIsAvailableTrue(bloodType);
        } else if (city != null) {
            donors = donorProfileRepository
                    .findByCityAndIsAvailableTrue(city);
        } else {
            donors = donorProfileRepository.findAll();
        }

        return donors.stream().map(this::mapToResponse).toList();
    }

    // ── Get all donors (admin) ─────────────────────────────
    public List<DonorProfileResponse> getAllDonors() {
        return donorProfileRepository.findAll()
                .stream().map(this::mapToResponse).toList();
    }

    // ── Toggle availability ────────────────────────────────
    public DonorProfileResponse toggleAvailability(Long userId) {
        DonorProfile profile = donorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Donor profile not found"));
        profile.setAvailable(!profile.isAvailable());
        donorProfileRepository.save(profile);
        return mapToResponse(profile);
    }

    // ── Mapper ─────────────────────────────────────────────
    private DonorProfileResponse mapToResponse(DonorProfile profile) {
        return DonorProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .fullName(profile.getUser().getFullName())
                .email(profile.getUser().getEmail())
                .profileImageUrl(profile.getUser().getProfileImageUrl())
                .bloodType(profile.getBloodType())
                .city(profile.getCity())
                .phone(profile.getPhone())
                .isAvailable(profile.isAvailable())
                .lastDonationDate(profile.getLastDonationDate())
                .totalDonations(profile.getTotalDonations())
                .createdAt(profile.getCreatedAt())
                .build();
    }
}