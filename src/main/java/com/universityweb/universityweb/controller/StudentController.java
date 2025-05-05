package com.universityweb.universityweb.controller;

import com.universityweb.universityweb.dto.AcademicianDTO;
import com.universityweb.universityweb.dto.StudentDTO;
import com.universityweb.universityweb.entity.Academician;
import com.universityweb.universityweb.entity.Note;
import com.universityweb.universityweb.entity.Student;
import com.universityweb.universityweb.repository.StudentRepository;
import com.universityweb.universityweb.security.JwtUtil;
import com.universityweb.universityweb.service.AcademicianService;
import com.universityweb.universityweb.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AcademicianService academicianService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    @PreAuthorize("hasRole('ACADEMICIAN')")
    public List<Student> getAll() {
        return studentService.getAllStudents();
    }

    @GetMapping("/by-email/{email}")
    @PreAuthorize("hasAnyRole('ACADEMICIAN', 'STUDENT')")
    public ResponseEntity<StudentDTO> getByEmail(@PathVariable String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Öğrenci bulunamadı"));

        StudentDTO dto = StudentDTO.builder()
                .id(student.getId())
                .firstName(student.getStudentFirstName())
                .lastName(student.getStudentLastName())
                .email(student.getEmail())
                .departmentId(student.getDepartment().getId())
                .academicianId(student.getAcademician().getId())
                .build();

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/for-academician")
    @PreAuthorize("hasRole('ACADEMICIAN')")
    public ResponseEntity<List<Student>> getStudentsForAcademician(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token bulunamadı");
        }
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);
        Optional<Academician> academician = academicianService.getAcademicianByEmail(email);
        List<Student> students = studentService.getStudentsByAcademicianId(academician.get().getId());
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ACADEMICIAN', 'STUDENT')")
    public Student getById(@PathVariable Long id) {
        return studentService.getStudentById(id).orElse(null);
    }

    @PostMapping
    @PreAuthorize("hasRole('ACADEMICIAN')")
    public Student create(@RequestBody Student student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        return studentService.createStudent(student);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ACADEMICIAN')")
    public ResponseEntity<Student> updateStudents(@PathVariable Long id, @RequestBody Student updatedStudent) {

        Student student = studentService.updateStudent(id,updatedStudent);
        return ResponseEntity.ok(student);
    }

//    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ACADEMICIAN')")
//    public ResponseEntity<Student> updateStudentsDepartment(@PathVariable Long id, @RequestBody Student departmentId) {
//
//        Student student = studentService.updateStudentDepartment(id,departmentId);
//        return ResponseEntity.ok(student);
//    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ACADEMICIAN')")
    public void delete(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}