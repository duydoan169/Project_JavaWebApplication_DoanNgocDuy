package org.example.project.controller;

import org.example.project.dto.MedicineDTO;
import org.example.project.exception.ServiceException;
import org.example.project.model.Medicine;
import org.example.project.service.MedicineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    @GetMapping
    public String list(Model model) {
        List<Medicine> medicines = medicineService.getAllMedicines();
        model.addAttribute("medicines", medicines);
        model.addAttribute("activePage", "medicines");
        return "admin/medicines/list";
    }

    @GetMapping("/add")
    public String addPage(Model model) {
        model.addAttribute("medicineDTO", new MedicineDTO());
        model.addAttribute("activePage", "medicines");
        return "admin/medicines/form";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("medicineDTO") MedicineDTO dto,
                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "admin/medicines/form";

        try {
            medicineService.save(dto);
            return "redirect:/admin/medicines";
        } catch (ServiceException e) {
            bindingResult.rejectValue(e.getField(), "", e.getMessage());
            return "admin/medicines/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        Medicine medicine = medicineService.findMedicineById(id);
        MedicineDTO dto = new MedicineDTO();
        dto.setId(medicine.getId());
        dto.setName(medicine.getName());
        dto.setUnit(medicine.getUnit());
        dto.setStockQuantity(medicine.getStockQuantity());
        dto.setPricePerUnit(medicine.getPricePerUnit());
        model.addAttribute("medicineDTO", dto);
        model.addAttribute("activePage", "medicines");
        return "admin/medicines/form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute("medicineDTO") MedicineDTO dto,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "admin/medicines/form";

        try {
            medicineService.update(id, dto);
            return "redirect:/admin/medicines";
        } catch (ServiceException e) {
            bindingResult.rejectValue(e.getField(), "", e.getMessage());
            return "admin/medicines/form";
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            medicineService.delete(id);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/medicines";
    }

}