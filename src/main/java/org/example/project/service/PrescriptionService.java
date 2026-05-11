package org.example.project.service;

import lombok.RequiredArgsConstructor;
import org.example.project.exception.ServiceException;
import org.example.project.model.Medicine;
import org.example.project.model.Prescription;
import org.example.project.model.PrescriptionDetail;
import org.example.project.model.enums.PrescriptionStatus;
import org.example.project.repository.MedicineRepository;
import org.example.project.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final MedicineRepository medicineRepository;

    public List<Prescription> getPendingPrescriptions() {
        return prescriptionRepository.findByStatusAndDetailsIsNotEmptyOrderByCreatedAtDesc(PrescriptionStatus.PENDING);
    }

    @Transactional
    public void dispense(Long prescriptionId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId).orElse(null);

        for (PrescriptionDetail detail : prescription.getDetails()) {
            Medicine medicine = detail.getMedicine();
            if (medicine.getStockQuantity() < detail.getQuantity()) {
                throw new ServiceException(null,
                        "Không đủ tồn kho: " + medicine.getName() +
                                " (còn " + medicine.getStockQuantity() + " " + medicine.getUnit() + ")");
            }
        }

        for (PrescriptionDetail detail : prescription.getDetails()) {
            Medicine medicine = detail.getMedicine();
            medicine.setStockQuantity(medicine.getStockQuantity() - detail.getQuantity());
            medicineRepository.save(medicine);
        }

        prescription.setStatus(PrescriptionStatus.DISPENSED);
        prescriptionRepository.save(prescription);
    }
}