package com.universityweb.universityweb.controller;

import com.universityweb.universityweb.entity.Academician;
import com.universityweb.universityweb.entity.Note;
import com.universityweb.universityweb.entity.Student;
import com.universityweb.universityweb.security.JwtUtil;
import com.universityweb.universityweb.service.NoteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.universityweb.universityweb.service.AcademicianService;
import com.universityweb.universityweb.service.StudentService;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private AcademicianService academicianService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private JwtUtil jwtUtil;
    @GetMapping
    @PreAuthorize("hasAnyRole('ACADEMICIAN', 'STUDENT')")
    public List<Note> getAll() {
        return noteService.getAllNotes();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ACADEMICIAN', 'STUDENT')")
    public Note getById(@PathVariable Long id) {
        return noteService.getNoteById(id).orElse(null);
    }

//    @GetMapping("/student/{studentId}")
//    @PreAuthorize("hasRole('STUDENT')")
//    public List<Note> getByStudentId(@PathVariable Long studentId) {
//        return noteService.getNotesByStudentId(studentId);
//    }
//
//    @GetMapping("/academician/{academicianId}")
//    @PreAuthorize("hasRole('ACADEMICIAN')")
//    public List<Note> getByAcademicianId(@PathVariable Long academicianId) {
//        return noteService.getNotesByAcademicianId(academicianId);
//    }



    @PostMapping("/create")
    @PreAuthorize("hasRole('ACADEMICIAN')")
    public ResponseEntity<Note> createNote(@RequestBody Note note, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            System.out.println("Authenticated user: " + auth.getName());
            System.out.println("Authorities: " + auth.getAuthorities());
        } else {
            System.out.println("No authenticated user found.");
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token bulunamadı");
        }
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);

        Academician academician = academicianService.getAcademicianByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Akademisyen bulunamadı"));

        note.setAcademician(academician);
        Note savedNote = noteService.createNote(note);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNote);
    }

    @GetMapping("/for-academician")
    @PreAuthorize("hasRole('ACADEMICIAN')")
    public ResponseEntity<List<Note>> getNotesForAcademician(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token bulunamadı");
        }
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);
        Optional<Academician> academician = academicianService.getAcademicianByEmail(email);
        List<Note> notes = noteService.getNotesByAcademicianId(academician.get().getId());
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/for-student")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<Note>> getNotesForStudent(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token bulunamadı");
        }
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);


        Student student = studentService.getStudentByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Öğrenci bulunamadı"));


        Academician teacher = student.getAcademician();
        if (teacher == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Öğretmen bulunamadı");
        }


        List<Note> notes = noteService.getNotesByAcademicianId(teacher.getId());
        return ResponseEntity.ok(notes);
    }

    @PostMapping
    @PreAuthorize("hasRole('ACADEMICIAN')")
    public Note create(@RequestBody Note note) {
        return noteService.createNote(note);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ACADEMICIAN')")
    public ResponseEntity<Note> updateNotes(@PathVariable Long id, @RequestBody Note updatedNote) {

        Note note = noteService.updateNote(id,updatedNote);
        return ResponseEntity.ok(note);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ACADEMICIAN')")
    public void delete(@PathVariable Long id) {
        noteService.deleteNote(id);
    }
}