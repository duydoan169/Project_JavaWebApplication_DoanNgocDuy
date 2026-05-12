package org.example.project.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.project.model.User;
import org.example.project.model.enums.Role;
import org.springframework.web.servlet.HandlerInterceptor;

public class Interceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String path = request.getRequestURI();

        User currentUser = (User) request.getSession().getAttribute("currentUser");

        if (currentUser == null) {
            response.sendRedirect("/login");
            return false;
        }

        Role role = currentUser.getRole();

        if (path.startsWith("/admin")) {
            if (role == Role.DOCTOR) {
                response.sendRedirect("/doctor/home");
                return false;
            } else if (role == Role.PATIENT) {
                response.sendRedirect("/home");
                return false;
            }
        }

        if (path.startsWith("/doctor")) {
            if (role == Role.ADMIN) {
                response.sendRedirect("/admin/home");
                return false;
            } else if (role == Role.PATIENT) {
                response.sendRedirect("/home");
                return false;
            }
        }

        if (path.startsWith("/appointments") || path.startsWith("/medical-records") || path.equals("/profile") || path.equals("/home")) {
            if (role == Role.ADMIN) {
                response.sendRedirect("/admin/home");
                return false;
            } else if (role == Role.DOCTOR) {
                response.sendRedirect("/doctor/home");
                return false;
            }
        }

        return true;
    }
}