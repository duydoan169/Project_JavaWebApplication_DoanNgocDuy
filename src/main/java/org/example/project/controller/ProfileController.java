package org.example.project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.project.dto.DoctorProfileDTO;
import org.example.project.dto.ProfileDTO;
import org.example.project.exception.ServiceException;
import org.example.project.model.Doctor;
import org.example.project.model.User;
import org.example.project.repository.DoctorRepository;
import org.example.project.repository.SpecialtyRepository;
import org.example.project.service.AppointmentService;
import org.example.project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final AppointmentService appointmentService;

    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");

        ProfileDTO dto = new ProfileDTO();
        dto.setFullName(currentUser.getFullName());
        dto.setPhone(currentUser.getPhone());
        dto.setDateOfBirth(currentUser.getDateOfBirth());
        dto.setGender(currentUser.getGender());
        dto.setAddress(currentUser.getAddress());

        model.addAttribute("profileDTO", dto);
        return "user/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute("profileDTO") ProfileDTO dto,
                                BindingResult bindingResult,
                                HttpSession session) {
        if (bindingResult.hasErrors()) return "user/profile";

        User currentUser = (User) session.getAttribute("currentUser");
        try {
            userService.updateProfile(currentUser.getId(), dto);
            // update session with new info
            currentUser.setFullName(dto.getFullName());
            currentUser.setPhone(dto.getPhone());
            currentUser.setDateOfBirth(dto.getDateOfBirth());
            currentUser.setGender(dto.getGender());
            currentUser.setAddress(dto.getAddress());
            session.setAttribute("currentUser", currentUser);
            return "redirect:/profile?success";
        } catch (ServiceException e) {
            bindingResult.rejectValue(e.getField(), "", e.getMessage());
            return "user/profile";
        }
    }

    @GetMapping("/doctor/profile")
    public String doctorProfilePage(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        Doctor doctor = userService.findDoctorByUserId(currentUser.getId());

        DoctorProfileDTO dto = new DoctorProfileDTO();
        dto.setFullName(currentUser.getFullName());
        dto.setPhone(currentUser.getPhone());
        dto.setDateOfBirth(currentUser.getDateOfBirth());
        dto.setGender(currentUser.getGender());
        dto.setAddress(currentUser.getAddress());
        dto.setSpecialtyId(doctor.getSpecialty().getId());

        model.addAttribute("doctorProfileDTO", dto);
        model.addAttribute("specialties", appointmentService.getAllSpecialties());
        return "doctor/doctor-profile";
    }

    @PostMapping("/doctor/profile")
    public String updateDoctorProfile(@Valid @ModelAttribute("doctorProfileDTO") DoctorProfileDTO dto,
                                      BindingResult bindingResult,
                                      HttpSession session,
                                      Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("specialties", appointmentService.getAllSpecialties());
            return "doctor/doctor-profile";
        }

        User currentUser = (User) session.getAttribute("currentUser");
        try {
            userService.updateDoctorProfile(currentUser.getId(), dto);
            currentUser.setFullName(dto.getFullName());
            currentUser.setPhone(dto.getPhone());
            currentUser.setDateOfBirth(dto.getDateOfBirth());
            currentUser.setGender(dto.getGender());
            currentUser.setAddress(dto.getAddress());
            session.setAttribute("currentUser", currentUser);
            return "redirect:/doctor/profile?success";
        } catch (ServiceException e) {
            bindingResult.rejectValue(e.getField(), "", e.getMessage());
            model.addAttribute("specialties", appointmentService.getAllSpecialties());
            return "doctor/doctor-profile";
        }
    }
}