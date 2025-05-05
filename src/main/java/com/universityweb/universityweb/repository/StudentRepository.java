package com.universityweb.universityweb.repository;

import com.universityweb.universityweb.entity.Academician;
import com.universityweb.universityweb.entity.Note;
import com.universityweb.universityweb.entity.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String studentEmail);
    List<Student> findByAcademicianId(Long academicianId);
}
