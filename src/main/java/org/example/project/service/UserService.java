package org.example.project.service;

import org.example.project.dto.DoctorProfileDTO;
import org.example.project.dto.LoginRequest;
import org.example.project.dto.ProfileDTO;
import org.example.project.dto.RegisterRequest;
import org.example.project.model.Doctor;
import org.example.project.model.Specialty;
import org.example.project.model.User;
import org.example.project.model.enums.Role;
import org.example.project.exception.ServiceException;
import org.example.project.repository.DoctorRepository;
import org.example.project.repository.SpecialtyRepository;
import org.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final SpecialtyRepository specialtyRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {
        if (userRepository.existsUserByUsername(request.getUsername().trim())) {
            throw new ServiceException("username", "Tên người dùng đã tồn tại");
        }
        if (userRepository.existsUserByEmail(request.getEmail().trim().toLowerCase())) {
            throw new ServiceException("email", "Email đã tồn tại");
        }

        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName().trim());
        user.setRole(Role.PATIENT);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    public User login(LoginRequest request) {
        User user = userRepository.findUserByEmail(request.getEmail().trim().toLowerCase());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new ServiceException("login", "Email hoặc mật khẩu không chính xác");
        }
        return user;
    }

    public void updateProfile(Long userId, ProfileDTO dto) {
        User user = userRepository.findUserById(userId);
        if (user == null) throw new ServiceException(null, "Người dùng không tồn tại");

        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setGender(dto.getGender());
        user.setAddress(dto.getAddress());
        userRepository.save(user);
    }

    public void updateDoctorProfile(Long userId, DoctorProfileDTO dto) {
        User user = userRepository.findUserById(userId);
        if (user == null) throw new ServiceException(null, "Người dùng không tồn tại");

        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setGender(dto.getGender());
        user.setAddress(dto.getAddress());
        userRepository.save(user);

        Doctor doctor = doctorRepository.findDoctorByUserId(userId);
        if (doctor == null) throw new ServiceException(null, "Bác sĩ dùng không tồn tại");

        Specialty specialty = specialtyRepository.findSpecialtyById(dto.getSpecialtyId());
        if (specialty == null) throw new ServiceException("specialtyId", "Chuyên môn không tồn tại");

        doctor.setSpecialty(specialty);
        doctorRepository.save(doctor);
    }

    public Doctor findDoctorByUserId(Long userId){
        return doctorRepository.findDoctorByUserId(userId);
    }
}
