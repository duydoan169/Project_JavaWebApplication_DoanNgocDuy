package org.example.project.service;

import lombok.RequiredArgsConstructor;
import org.example.project.model.Specialty;
import org.example.project.repository.SpecialtyRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    public List<Specialty> getAllSpecialties(Model model){
        return specialtyRepository.findAll();
    }
}

