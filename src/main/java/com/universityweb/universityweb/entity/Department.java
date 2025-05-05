package com.universityweb.universityweb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "department")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="name")
    private String departmentName;

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private List<Academician> academicians;

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private List<Student> students;
}