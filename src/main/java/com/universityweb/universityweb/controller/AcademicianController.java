package com.universityweb.universityweb.controller;

import com.universityweb.universityweb.dto.AcademicianDTO;
import com.universityweb.universityweb.entity.Academician;
import com.universityweb.universityweb.service.AcademicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.universityweb.universityweb.repository.AcademicianRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/academicians")
public class AcademicianController {

    @Autowired
    private AcademicianService academicianService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AcademicianRepository academicianRepository;

    @GetMapping
    @PreAuthorize("hasRole('ACADEMICIAN')")
    public List<Academician> getAll() {
        return academicianService.getAllAcademicians();
    }

//    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('ACADEMICIAN')")
//    public Academician getById(@PathVariable Long id) {
//        return academicianService.getAcademicianById(id).orElse(null);
//    }

    @GetMapping("/by-email/{email}")
    @PreAuthorize("hasRole('ACADEMICIAN')")
    public ResponseEntity<AcademicianDTO> getByEmail(@PathVariable String email) {
        Academician academician = academicianRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Akademisyen bulunamadı"));

        AcademicianDTO dto = AcademicianDTO.builder()
                .id(academician.getId())
                .firstName(academician.getAcademicianFirstName())
                .lastName(academician.getAcademicianLastName())
                .email(academician.getEmail())
                .departmentId(academician.getDepartment().getId())
                .build();

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @PreAuthorize("hasRole('ACADEMICIAN')")
    public Academician create(@RequestBody Academician academician) {
        academician.setPassword(passwordEncoder.encode(academician.getPassword()));
        return academicianService.createAcademician(academician);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcademicianDTO> getAcademicianById(@PathVariable Long id) {
        Academician academician = academicianRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Akademisyen bulunamadı"));
        AcademicianDTO dto = AcademicianDTO.builder()
                .id(academician.getId())
                .firstName(academician.getAcademicianFirstName())
                .lastName(academician.getAcademicianLastName())
                .email(academician.getEmail())
                .departmentId(academician.getDepartment().getId())
                .build();
        return ResponseEntity.ok(dto);
    }

    @PutMapping
    @PreAuthorize("hasRole('ACADEMICIAN')")
    public Academician update(@RequestBody Academician academician) {
        return academicianService.updateAcademician(academician);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ACADEMICIAN')")
    public void delete(@PathVariable Long id) {
        academicianService.deleteAcademician(id);
    }
}