package com.universityweb.universityweb.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "note")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="title")
    private String title;
    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "academician", nullable = false)
    private Academician academician;

//    @ManyToOne(optional = true)
//    @JoinColumn(name = "student", nullable = false)
//    private Student student;
}
