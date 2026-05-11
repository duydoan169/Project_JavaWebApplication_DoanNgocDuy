package org.example.project.repository;

import org.example.project.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    MedicalRecord findMedicalRecordByAppointmentId(Long appointmentId);
}