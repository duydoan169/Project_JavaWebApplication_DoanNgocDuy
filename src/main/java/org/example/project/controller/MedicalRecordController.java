package org.example.project.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.project.model.User;
import org.example.project.service.AppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/medical-records")
public class MedicalRecordController {

    private final AppointmentService appointmentService;

    @GetMapping
    public String medicalHistory(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        model.addAttribute("appointments", appointmentService.getMedicalHistory(currentUser.getId()));
        return "user/medical-record/medical-records";
    }

    @GetMapping("/{id}")
    public String medicalRecordDetail(@PathVariable Long id, Model model) {
        model.addAttribute("appointment", appointmentService.findAppointmentById(id));
        model.addAttribute("medicalRecord", appointmentService.findMedicalRecord(id));
        model.addAttribute("prescription", appointmentService.findPrescription(id));
        return "user/medical-record/medical-record-detail";
    }
}