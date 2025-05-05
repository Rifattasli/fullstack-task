package com.universityweb.universityweb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="student")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="name")
    private String studentFirstName;
    @Column(name="surname")
    private String studentLastName;
    @Column(name="email",unique = true, nullable = false)
    private String email;
    @Column(name="password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "department", nullable = false)
    private Department department;

    @ManyToOne

    @JoinColumn(name = "academician", nullable = false)
    private Academician academician;

//    @OneToMany(mappedBy = "student")
//    @JsonIgnore
//    private List<Note> notes;
}