package org.example.project.service;

import lombok.RequiredArgsConstructor;
import org.example.project.model.TestType;
import org.example.project.repository.TestTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestTypeService {

    private final TestTypeRepository testTypeRepository;

    public List<TestType> getAllTestTypes(Model model){
        return testTypeRepository.findAll();
    }
}
