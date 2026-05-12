package org.example.project.controller;

import lombok.RequiredArgsConstructor;
import org.example.project.repository.SpecialtyRepository;
import org.example.project.service.AppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/specialties")
public class SpecialtyController {

   private final AppointmentService appointmentService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("specialties", appointmentService.getAllSpecialties());
        model.addAttribute("activePage", "specialties");
        return "admin/specialties/specialty-list";
    }
}
