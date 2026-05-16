// InventoryRepository.java
package com.bdms.blood_donation_backend.repository;
import com.bdms.blood_donation_backend.entity.Inventory;
import com.bdms.blood_donation_backend.enums.BloodType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByHospitalId(Long hospitalId);
    Optional<Inventory> findByHospitalIdAndBloodType(Long hospitalId, BloodType bloodType);
    long countByHospitalId(Long hospitalId);
}