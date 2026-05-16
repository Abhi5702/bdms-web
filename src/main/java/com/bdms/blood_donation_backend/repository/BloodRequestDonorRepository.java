// BloodRequestDonorRepository.java
package com.bdms.blood_donation_backend.repository;
import com.bdms.blood_donation_backend.entity.BloodRequestDonor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BloodRequestDonorRepository extends JpaRepository<BloodRequestDonor, Long> {
    List<BloodRequestDonor> findByRequestId(Long requestId);
    List<BloodRequestDonor> findByDonorId(Long donorId);
    boolean existsByRequestIdAndDonorId(Long requestId, Long donorId);
}