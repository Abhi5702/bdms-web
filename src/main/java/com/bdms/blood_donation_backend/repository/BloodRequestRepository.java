// BloodRequestRepository.java
package com.bdms.blood_donation_backend.repository;
import com.bdms.blood_donation_backend.entity.BloodRequest;
import com.bdms.blood_donation_backend.enums.BloodType;
import com.bdms.blood_donation_backend.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {
    List<BloodRequest> findByStatus(RequestStatus status);
    List<BloodRequest> findByHospitalId(Long hospitalId);
    List<BloodRequest> findByBloodTypeAndStatus(BloodType bloodType, RequestStatus status);
    long countByStatus(RequestStatus status);
    long countByHospitalId(Long hospitalId);
    long countByHospitalIdAndStatus(Long hospitalId, RequestStatus status);
    long countByBloodTypeAndStatus(BloodType bloodType, RequestStatus status);
}