package org.example.project.repository;

import jakarta.validation.constraints.NotNull;
import org.example.project.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Doctor findDoctorByUserId(Long id);
    List<Doctor> findBySpecialtyId(Long specialtyId);

    Doctor findDoctorById(Long doctorId);
}
