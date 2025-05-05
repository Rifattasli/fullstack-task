package com.universityweb.universityweb.repository;

import com.universityweb.universityweb.entity.Academician;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AcademicianRepository extends JpaRepository<Academician, Long>{
    Optional<Academician> findByEmail(String academicianEmail);
}
