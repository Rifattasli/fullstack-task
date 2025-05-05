package com.universityweb.universityweb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="academician")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Academician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="name")
    private String academicianFirstName;
    @Column(name="surname")
    private String academicianLastName;
    @Column(name="email",unique = true, nullable = false)
    private String email;
    @Column(name="password")
    private String password;

    @ManyToOne
    //@JsonBackReference
    @JoinColumn(name = "department", nullable = false)
    private Department department;

    @OneToMany(mappedBy = "academician")
    @JsonIgnore
    private List<Student> student;

    @OneToMany(mappedBy = "academician")
    @JsonIgnore
    private List<Note> note;
}