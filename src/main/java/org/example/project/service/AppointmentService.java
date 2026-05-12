package org.example.project.service;

import lombok.RequiredArgsConstructor;
import org.example.project.dto.AppointmentDTO;
import org.example.project.exception.ServiceException;
import org.example.project.model.*;
import org.example.project.model.enums.AppointmentStatus;
import org.example.project.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final SpecialtyRepository specialtyRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final TestTypeRepository testTypeRepository;

    private static final LocalTime SLOT_START = LocalTime.of(8, 0);
    private static final LocalTime SLOT_END   = LocalTime.of(16, 30);

    public List<Specialty> getAllSpecialties() {
        return specialtyRepository.findAll();
    }

    public List<TestType> getAllTestTypes() {
        return testTypeRepository.findAll();
    }

    public Specialty getSpecialtyById(Long id) {
        return specialtyRepository.findById(id).orElse(null);
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id).orElse(null);
    }

    public List<Doctor> getDoctorsBySpecialty(Long specialtyId) {
        return doctorRepository.findBySpecialtyId(specialtyId);
    }

    public List<LocalTime> getAvailableSlots(Long doctorId, LocalDate date) {
        List<Appointment> booked = appointmentRepository
                .findByDoctorIdAndAppointmentDateAndStatusNot(doctorId, date, AppointmentStatus.CANCELLED);

        List<LocalTime> bookedTimes = booked.stream()
                .map(Appointment::getStartTime)
                .toList();


        List<LocalTime> available = new ArrayList<>();
        LocalTime cursor = SLOT_START;

        while (!cursor.isAfter(SLOT_END)) {
            boolean isPast = date.isEqual(LocalDate.now())
                    && !cursor.isAfter(LocalTime.now());

            if (!bookedTimes.contains(cursor) && !isPast) {
                available.add(cursor);
            }
            cursor = cursor.plusMinutes(30);
        }

        return available;
    }

    public void book(AppointmentDTO dto, User patient) {
        boolean conflict = appointmentRepository
                .existsByDoctorIdAndAppointmentDateAndStartTimeAndStatusNot(
                        dto.getDoctorId(),
                        dto.getAppointmentDate(),
                        dto.getStartTime(),
                        AppointmentStatus.CANCELLED
                );
        if (conflict) {
            throw new ServiceException("startTime", "Khung giờ này đã được đặt, vui lòng chọn giờ khác");
        }

        Doctor doctor = doctorRepository.findDoctorById(dto.getDoctorId());

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setStartTime(dto.getStartTime());
        appointment.setEndTime(dto.getStartTime().plusMinutes(30));
        appointment.setReason(dto.getReason());
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setCreatedAt(LocalDateTime.now());

        appointmentRepository.save(appointment);
    }

    public List<Appointment> getListAppointments(Long patientId) {
        return appointmentRepository
                .findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    public Appointment findAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    public List<Appointment> getMedicalHistory(Long patientId) {
        return appointmentRepository
                .findByPatientIdAndStatusInOrderByCreatedAtDesc(
                        patientId,
                        List.of(AppointmentStatus.COMPLETED, AppointmentStatus.CANCELLED)
                );
    }

    public MedicalRecord findMedicalRecord(Long appointmentId) {
        return medicalRecordRepository.findMedicalRecordByAppointmentId(appointmentId);
    }

    public Prescription findPrescription(Long appointmentId) {
        return prescriptionRepository.findPrescriptionByAppointmentId(appointmentId);
    }

    public void cancelAppointment(Long appointmentId, String cancelReason) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setCancelReason(cancelReason != null ? cancelReason.trim() : null);
        appointmentRepository.save(appointment);
    }
}