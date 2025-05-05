package com.universityweb.universityweb.service;

import com.universityweb.universityweb.entity.Note;
import com.universityweb.universityweb.entity.Student;
import com.universityweb.universityweb.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public List<Student> getStudentsByAcademicianId(Long academicianId) {
        return studentRepository.findByAcademicianId(academicianId);
    }

    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public Student updateStudent(Long id, Student updatedStudent) {
        Student student = studentRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Bu id ile öğrenci bulunamadı: " + id));

        student.setStudentFirstName(updatedStudent.getStudentFirstName());
        student.setStudentLastName(updatedStudent.getStudentLastName());
        student.setEmail(updatedStudent.getEmail());
        student.setDepartment(updatedStudent.getDepartment());
        student.setAcademician(updatedStudent.getAcademician());
        return studentRepository.save(student);
    }

//    public Student updateStudentDepartment(Long id, Student updatedDepartmentId) {
//        Student student = studentRepository.findById(id).orElseThrow(()->
//                new ResourceNotFoundException("Bu id ile öğrenci bulunamadı: " + id));
//
//
//        student.setDepartment(updatedDepartmentId.getDepartment());
//        return studentRepository.save(student);
//    }
}