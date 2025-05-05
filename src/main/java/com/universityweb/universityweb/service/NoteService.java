package com.universityweb.universityweb.service;

import com.universityweb.universityweb.entity.Note;
import com.universityweb.universityweb.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }

//    public List<Note> getNotesByStudentId(Long studentId) {
//        return noteRepository.findByStudentId(studentId);
//    }

    public List<Note> getNotesByAcademicianId(Long academicianId) {
        return noteRepository.findByAcademicianId(academicianId);
    }

    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    public Note updateNote(Long id, Note updatedNote) {
        Note note = noteRepository.findById(id).orElseThrow(()->
               new ResourceNotFoundException("Bu id ile not bulunamadı: " + id));
        note.setContent(updatedNote.getContent());
        note.setTitle(updatedNote.getTitle());

       return noteRepository.save(note);
    }

}