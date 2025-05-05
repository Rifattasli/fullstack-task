package com.universityweb.universityweb.service;

import com.universityweb.universityweb.entity.Academician;
import com.universityweb.universityweb.entity.Student;
import com.universityweb.universityweb.repository.AcademicianRepository;
import com.universityweb.universityweb.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AcademicianRepository academicianRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Academician academician = academicianRepository.findByEmail(email).orElse(null);
        if (academician != null) {
            return User.builder()
                    .username(academician.getEmail())
                    .password(academician.getPassword())
                    .authorities("ROLE_ACADEMICIAN")
                    .build();
        }


        Student student = studentRepository.findByEmail(email).orElse(null);
        if (student != null) {
            return User.builder()
                    .username(student.getEmail())
                    .password(student.getPassword())
                    .authorities("ROLE_STUDENT")
                    .build();
        }

        throw new UsernameNotFoundException("Kullanıcı bulunamadı: " + email);
    }
}