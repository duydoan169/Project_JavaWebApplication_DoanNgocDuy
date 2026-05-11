package org.example.project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.project.dto.AppointmentDTO;
import org.example.project.exception.ServiceException;
import org.example.project.model.Appointment;
import org.example.project.model.User;
import org.example.project.service.AppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/book")
    public String step1Specialties(Model model) {
        model.addAttribute("specialties", appointmentService.getAllSpecialties());
        return "user/appointment/step-1";
    }

    @GetMapping("/book/doctors")
    public String step2Doctors(@RequestParam Long specialtyId, Model model) {
        model.addAttribute("doctors", appointmentService.getDoctorsBySpecialty(specialtyId));
        model.addAttribute("specialty", appointmentService.getSpecialtyById(specialtyId));
        return "user/appointment/step-2";
    }

    @GetMapping("/book/slots")
    public String step3Slots(@RequestParam Long doctorId,
                             @RequestParam(required = false) String date,
                             Model model) {
        LocalDate selectedDate = (date != null && !date.isBlank())
                ? LocalDate.parse(date)
                : LocalDate.now();

        model.addAttribute("doctor", appointmentService.getDoctorById(doctorId));
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("availableSlots", appointmentService.getAvailableSlots(doctorId, selectedDate));
        model.addAttribute("appointmentDTO", new AppointmentDTO());
        return "user/appointment/step-3";
    }

    @PostMapping("/book")
    public String book(@Valid @ModelAttribute("appointmentDTO") AppointmentDTO dto,
                       BindingResult bindingResult,
                       HttpSession session,
                       Model model) {

        if (bindingResult.hasErrors()) {
            repopulateStep3(dto, model);
            return "user/appointment/step-3";
        }

        User currentUser = (User) session.getAttribute("currentUser");
        try {
            appointmentService.book(dto, currentUser);
            return "redirect:/appointments/list?success";
        } catch (ServiceException e) {
            bindingResult.rejectValue(e.getField(), "", e.getMessage());
            repopulateStep3(dto, model);
            return "user/appointment/step-3";
        }
    }

    @GetMapping("/list")
    public String listAppointments(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        model.addAttribute("appointments", appointmentService.getListAppointments(currentUser.getId()));
        return "user/appointment/appointment-list";
    }

    private void repopulateStep3(AppointmentDTO dto, Model model) {
        model.addAttribute("doctor", appointmentService.getDoctorById(dto.getDoctorId()));
        model.addAttribute("selectedDate", dto.getAppointmentDate());
        model.addAttribute("availableSlots", appointmentService.getAvailableSlots(dto.getDoctorId(), dto.getAppointmentDate()));
    }

    @PostMapping("/cancel")
    public String cancel(@RequestParam Long appointmentId,
                         @RequestParam(required = false) String cancelReason) {
        appointmentService.cancelAppointment(appointmentId, cancelReason);
        return "redirect:/appointments/list?cancelled";
    }
}