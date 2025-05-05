package com.universityweb.universityweb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicianDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Long departmentId;
    // private String departmentName;
}