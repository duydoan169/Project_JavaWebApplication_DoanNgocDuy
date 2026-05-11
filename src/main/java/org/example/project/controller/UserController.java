package org.example.project.controller;

import jakarta.servlet.http.HttpSession;
import org.example.project.dto.LoginRequest;
import org.example.project.dto.RegisterRequest;
import org.example.project.exception.ServiceException;
import org.example.project.model.User;
import org.example.project.model.enums.Role;
import org.example.project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerRequest") RegisterRequest request,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "register";

        try {
            userService.register(request);
            return "redirect:/login";
        } catch (ServiceException e) {
            bindingResult.rejectValue(e.getField(), "", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginRequest") LoginRequest request,
                        BindingResult bindingResult,
                        HttpSession session) {
        if (bindingResult.hasErrors()) return "login";

        try {
            User user = userService.login(request);
            session.setAttribute("currentUser", user);

            if (user.getRole() == Role.ADMIN) {
                return "redirect:/admin/medicines";
            } else if (user.getRole() == Role.DOCTOR) {
                return "redirect:/doctor/home";
            } else {
                return "redirect:/home";
            }
        } catch (ServiceException e) {
            bindingResult.rejectValue("email", "", e.getMessage());
            bindingResult.rejectValue("password", "", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String homeUser(Model model, HttpSession session){
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        return "user/home";
    }

    @GetMapping("/doctor/home")
    public String homeDoctor(Model model, HttpSession session){
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        return "doctor/home";
    }
}