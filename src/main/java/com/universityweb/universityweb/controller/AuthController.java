package com.universityweb.universityweb.controller;

import com.universityweb.universityweb.entity.Department;
import com.universityweb.universityweb.paylaod.LoginPayload;
import com.universityweb.universityweb.service.AcademicianService;
import com.universityweb.universityweb.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.universityweb.universityweb.entity.Student;
import com.universityweb.universityweb.entity.Academician;
import com.universityweb.universityweb.repository.StudentRepository;
import com.universityweb.universityweb.repository.AcademicianRepository;
import com.universityweb.universityweb.security.JwtUtil;
import com.universityweb.universityweb.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.universityweb.universityweb.service.DepartmentService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final StudentRepository studentRepository;

    private final AcademicianRepository academicianRepository;

    private final BCryptPasswordEncoder passwordEncoder;
    private final AcademicianService academicianService;
    @Autowired
    private DepartmentService departmentService;

    private StudentService studentService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          StudentRepository studentRepository,
                          AcademicianRepository academicianRepository,
                          BCryptPasswordEncoder passwordEncoder, AcademicianService academicianService, DepartmentService departmentService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.studentRepository = studentRepository;
        this.academicianRepository = academicianRepository;
        this.passwordEncoder = passwordEncoder;
        this.academicianService = academicianService;
        this.departmentService = departmentService;
    }

    @GetMapping("/get-departments")
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/get-academicians")
    public ResponseEntity<List<Academician>> getAllAcademicians() {
        return ResponseEntity.ok().body(academicianService.getAllAcademicians());
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginPayload loginData) {
        String email = loginData.getEmail();
        String password = loginData.getPassword();
        String role = loginData.getRole();

        try {

            if ("student".equalsIgnoreCase(role)) {
                Student student = studentRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Öğrenci bulunamadı"));

                if (!passwordEncoder.matches(password, student.getPassword())) {
                    return ResponseEntity.status(401).body(Map.of("error", "Şifre hatalı"));
                }

            } else if ("academician".equalsIgnoreCase(role)) {
                Academician academician = academicianRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Akademisyen bulunamadı"));

                if (!passwordEncoder.matches(password, academician.getPassword())) {
                    return ResponseEntity.status(401).body(Map.of("error", "Şifre hatalı"));
                }

            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Geçersiz rol"));
            }


            String token = jwtUtil.generateToken(email);
            return ResponseEntity.ok(Map.of("token", token));

        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Giriş başarısız"));
        }
    }

    @PostMapping("/register/student")
    public Student registerStudent(@RequestBody Student student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        return studentRepository.save(student);
    }

    @PostMapping("/register/academician")
    public Academician registerAcademician(@RequestBody Academician academician) {
        academician.setPassword(passwordEncoder.encode(academician.getPassword()));
        return academicianRepository.save(academician);
    }
}