package org.example.project.controller;

import lombok.RequiredArgsConstructor;
import org.example.project.repository.TestTypeRepository;
import org.example.project.service.AppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/test-types")
public class TestTypeController {

    private final AppointmentService appointmentService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("testTypes", appointmentService.getAllTestTypes());
        model.addAttribute("activePage", "testTypes");
        return "admin/test-types/test-type-list";
    }
}