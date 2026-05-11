package org.example.project.service;

import lombok.RequiredArgsConstructor;
import org.example.project.dto.MedicineDTO;
import org.example.project.exception.ServiceException;
import org.example.project.model.Medicine;
import org.example.project.repository.MedicineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineService {

    private final MedicineRepository medicineRepository;

    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    public Medicine findMedicineById(Long id) {
        Medicine medicine = medicineRepository.findMedicineById(id);
        if (medicine == null) throw new ServiceException(null, "Không tìm thấy thuốc");
        return medicine;
    }

    public void save(MedicineDTO dto) {
        if (medicineRepository.existsByName(dto.getName())) {
            throw new ServiceException("name", "Tên thuốc đã tồn tại");
        }

        Medicine medicine = new Medicine();
        medicine.setName(dto.getName());
        medicine.setUnit(dto.getUnit());
        medicine.setStockQuantity(dto.getStockQuantity());
        medicine.setPricePerUnit(dto.getPricePerUnit());
        medicineRepository.save(medicine);
    }

    public void update(Long id, MedicineDTO dto) {
        Medicine medicine = findMedicineById(id);

        if (!medicine.getName().equals(dto.getName()) && medicineRepository.existsByName(dto.getName())) {
            throw new ServiceException("name", "Tên thuốc đã tồn tại");
        }

        medicine.setName(dto.getName());
        medicine.setUnit(dto.getUnit());
        medicine.setStockQuantity(dto.getStockQuantity());
        medicine.setPricePerUnit(dto.getPricePerUnit());
        medicineRepository.save(medicine);
    }

    public void delete(Long id) {
        findMedicineById(id);
        medicineRepository.deleteById(id);
    }
}
