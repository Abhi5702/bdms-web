// AppointmentRepository.java
package com.bdms.blood_donation_backend.repository;
import com.bdms.blood_donation_backend.entity.Appointment;
import com.bdms.blood_donation_backend.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDonorId(Long donorId);
    List<Appointment> findByHospitalId(Long hospitalId);
    long countByStatus(AppointmentStatus status);
    long countByHospitalIdAndStatus(Long hospitalId, AppointmentStatus status);
    long countByDonorIdAndStatus(Long donorId, AppointmentStatus status);
}