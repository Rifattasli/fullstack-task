package com.universityweb.universityweb.service;

import com.universityweb.universityweb.entity.Academician;
import com.universityweb.universityweb.repository.AcademicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class AcademicianService {

    @Autowired
    private AcademicianRepository academicianRepository;

    public List<Academician> getAllAcademicians() {
        return academicianRepository.findAll();
    }

    public Optional<Academician> getAcademicianById(Long id) {
        return academicianRepository.findById(id);
    }

    public Optional<Academician> getAcademicianByEmail(String email) {
        return academicianRepository.findByEmail(email);
    }

    public Academician createAcademician(Academician academician) {
        return academicianRepository.save(academician);
    }

    public void deleteAcademician(Long id) {
        academicianRepository.deleteById(id);
    }

    public Academician updateAcademician(Academician academician) {
        return academicianRepository.save(academician);
    }
}
