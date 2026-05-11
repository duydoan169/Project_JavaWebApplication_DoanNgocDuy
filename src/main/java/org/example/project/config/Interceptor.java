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

        if (path.startsWith("/admin") && role != Role.ADMIN) {
            if (role == Role.DOCTOR){
                response.sendRedirect("doctor/home");
            }else {
                response.sendRedirect("home");
                return false;
            }
        }

        if (path.startsWith("/doctor") && role != Role.DOCTOR) {
            if (role == Role.ADMIN){
                response.sendRedirect("admin/medicines/list");
            }else {
                response.sendRedirect("home");
                return false;
            }
        }

        if(!path.startsWith("/admin") && !path.startsWith("/doctor")){
            if (role == Role.ADMIN){
                response.sendRedirect("admin/medicines/list");
            }else if (role == Role.DOCTOR){
                response.sendRedirect("doctor/home");
            }
        }
        return true;
    }
}
