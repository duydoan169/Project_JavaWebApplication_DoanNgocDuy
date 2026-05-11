package org.example.project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.project.dto.ExaminationDTO;
import org.example.project.exception.ServiceException;
import org.example.project.model.Appointment;
import org.example.project.model.Doctor;
import org.example.project.model.User;
import org.example.project.repository.DoctorRepository;
import org.example.project.service.ExaminationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/doctor/examination")
public class ExaminationController {

    private final ExaminationService examinationService;
    private final DoctorRepository doctorRepository;

    // Today's pending appointment list
    @GetMapping("/list")
    public String pendingList(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        Doctor doctor = doctorRepository.findDoctorByUserId(currentUser.getId());
        model.addAttribute("appointments", examinationService.getPendingAppointments(doctor.getId()));
        return "doctor/examination-list";
    }

    // Examination form for one appointment
    @GetMapping("/{appointmentId}")
    public String examinationForm(@PathVariable Long appointmentId, Model model) {
        Appointment appointment = examinationService.getAppointmentById(appointmentId);

        ExaminationDTO dto = new ExaminationDTO();
        dto.setAppointmentId(appointmentId);

        model.addAttribute("appointment", appointment);
        model.addAttribute("examinationDTO", dto);
        model.addAttribute("medicines", examinationService.getAllMedicines());
        return "doctor/examination-form";
    }

    // Submit examination
    @PostMapping("/submit")
    public String submit(@Valid @ModelAttribute("examinationDTO") ExaminationDTO dto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            repopulateForm(dto, model);
            return "doctor/examination-form";
        }

        try {
            examinationService.examine(dto);
            return "redirect:/doctor/examination/list?success";
        } catch (ServiceException e) {
            bindingResult.rejectValue(e.getField(), "", e.getMessage());
            repopulateForm(dto, model);
            return "doctor/examination-form";
        }
    }

    private void repopulateForm(ExaminationDTO dto, Model model) {
        model.addAttribute("appointment", examinationService.getAppointmentById(dto.getAppointmentId()));
        model.addAttribute("medicines", examinationService.getAllMedicines());
    }
}