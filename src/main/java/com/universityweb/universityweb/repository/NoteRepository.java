package com.universityweb.universityweb.repository;

import com.universityweb.universityweb.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
   // List<Note> findByStudentId(Long studentId);
    List<Note> findByAcademicianId(Long academicianId);
    }