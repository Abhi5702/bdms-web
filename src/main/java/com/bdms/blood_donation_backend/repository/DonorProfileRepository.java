// DonorProfileRepository.java
package com.bdms.blood_donation_backend.repository;
import com.bdms.blood_donation_backend.entity.DonorProfile;
import com.bdms.blood_donation_backend.enums.BloodType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DonorProfileRepository extends JpaRepository<DonorProfile, Long> {
    Optional<DonorProfile> findByUserId(Long userId);
    List<DonorProfile> findByBloodTypeAndIsAvailableTrue(BloodType bloodType);
    List<DonorProfile> findByCityAndIsAvailableTrue(String city);
    List<DonorProfile> findByBloodTypeAndCityAndIsAvailableTrue(BloodType bloodType, String city);
    long countByIsAvailableTrue();
}