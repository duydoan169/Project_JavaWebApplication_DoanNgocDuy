package org.example.project.service;

import lombok.RequiredArgsConstructor;
import org.example.project.dto.ExaminationDTO;
import org.example.project.dto.PrescriptionDetailDTO;
import org.example.project.exception.ServiceException;
import org.example.project.model.*;
import org.example.project.model.enums.AppointmentStatus;
import org.example.project.model.enums.PrescriptionStatus;
import org.example.project.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExaminationService {

    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final MedicineRepository medicineRepository;

    public List<Appointment> getPendingAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorIdAndAppointmentDateAndStatusOrderByStartTimeAsc(
                doctorId, LocalDate.now(), AppointmentStatus.PENDING
        );
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    @Transactional
    public void examine(ExaminationDTO dto) {
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId()).orElse(null);

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);

        MedicalRecord record = new MedicalRecord();
        record.setAppointment(appointment);
        record.setSymptoms(dto.getSymptoms());
        record.setDiagnosis(dto.getDiagnosis());
        record.setNotes(dto.getNotes());
        record.setCreatedAt(LocalDateTime.now());
        medicalRecordRepository.save(record);

        Prescription prescription = new Prescription();
        prescription.setAppointment(appointment);
        prescription.setInstructions(dto.getInstructions());
        prescription.setCreatedAt(LocalDateTime.now());

        List<PrescriptionDetail> details = new ArrayList<>();
        if (dto.getMedicines() != null) {
            for (PrescriptionDetailDTO line : dto.getMedicines()) {
                if (line.getMedicineId() == null) continue;

                if (line.getQuantity() == null || line.getQuantity() < 1) {
                    throw new ServiceException("medicines", "Vui lòng nhập số lượng cho tất cả thuốc đã chọn");
                }
                if (line.getDosage() == null || line.getDosage().isBlank()) {
                    throw new ServiceException("medicines", "Vui lòng nhập liều dùng cho tất cả thuốc đã chọn");
                }

                Medicine medicine = medicineRepository.findById(line.getMedicineId()).orElse(null);

                PrescriptionDetail detail = new PrescriptionDetail();
                detail.setPrescription(prescription);
                detail.setMedicine(medicine);
                detail.setQuantity(line.getQuantity());
                detail.setDosage(line.getDosage());
                details.add(detail);
            }
        }
        if (details.isEmpty()) {
            prescription.setStatus(PrescriptionStatus.DISPENSED);
        } else {
            prescription.setStatus(PrescriptionStatus.PENDING);
        }
        prescription.setDetails(details);
        prescriptionRepository.save(prescription);
    }
}