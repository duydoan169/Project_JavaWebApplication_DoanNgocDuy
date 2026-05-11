package org.example.project.repository;

import org.example.project.model.Prescription;
import org.example.project.model.enums.PrescriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    Prescription findPrescriptionByAppointmentId(Long appointmentId);
    List<Prescription> findByStatus(PrescriptionStatus status);

    List<Prescription> findByStatusAndDetailsIsNotEmptyOrderByCreatedAtDesc(PrescriptionStatus prescriptionStatus);
}
