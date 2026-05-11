package org.example.project.controller;

import lombok.RequiredArgsConstructor;
import org.example.project.exception.ServiceException;
import org.example.project.service.PrescriptionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @GetMapping
    public String pendingPrescriptions(Model model) {
        model.addAttribute("prescriptions", prescriptionService.getPendingPrescriptions());
        model.addAttribute("activePage", "medicines");
        return "admin/prescriptions/prescription-list";
    }

    @PostMapping("/dispense")
    public String dispense(@RequestParam Long prescriptionId,
                           RedirectAttributes redirectAttributes) {
        try {
            prescriptionService.dispense(prescriptionId);
            return "redirect:/admin/prescriptions?success";
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/prescriptions?error";
        }
    }
}