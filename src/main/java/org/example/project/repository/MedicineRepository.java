package org.example.project.repository;

import org.example.project.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    Medicine findMedicineById(Long id);
    boolean existsByName(String name);
}
