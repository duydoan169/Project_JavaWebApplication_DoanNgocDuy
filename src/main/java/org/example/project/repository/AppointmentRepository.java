package org.example.project.repository;

import jakarta.validation.constraints.NotNull;
import org.example.project.model.Appointment;
import org.example.project.model.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByDoctorIdAndAppointmentDateAndStartTimeAndStatusNot(
            Long doctorId,
            LocalDate appointmentDate,
            LocalTime startTime,
            AppointmentStatus status
    );

    List<Appointment> findByDoctorIdAndAppointmentDateAndStatusNot(
            Long doctorId,
            LocalDate appointmentDate,
            AppointmentStatus status
    );

    List<Appointment> findByPatientIdOrderByAppointmentDateDescStartTimeDesc(Long patientId);

    List<Appointment> findByDoctorIdAndAppointmentDateAndStatus(Long doctorId, LocalDate date, AppointmentStatus status);

    Appointment findAppointmentById(Long appointmentId);

    List<Appointment> findByPatientIdAndStatusInOrderByAppointmentDateDescStartTimeDesc(
            Long patientId, List<AppointmentStatus> statuses
    );

    List<Appointment> findByPatientIdOrderByCreatedAtDesc(Long patientId);


    List<Appointment> findByDoctorIdAndAppointmentDateAndStatusOrderByStartTimeAsc(Long doctorId, LocalDate now, AppointmentStatus appointmentStatus);

    List<Appointment> findByPatientIdAndStatusInOrderByCreatedAtDesc(Long patientId, List<AppointmentStatus> completed);
}