package com.universityweb.universityweb.controller;

import com.universityweb.universityweb.entity.Department;
import com.universityweb.universityweb.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<Department>> getAll() {
        return ResponseEntity.ok().body(departmentService.getAllDepartments());
    }

    @GetMapping("/{id}")
    public Department getById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id).orElse(null);
    }

    @PostMapping
   // @PreAuthorize("hasRole('ACADEMICIAN')")
    public Department create(@RequestBody Department department) {
        return departmentService.createDepartment(department);
    }

    @PutMapping
   // @PreAuthorize("hasRole('ACADEMICIAN')")
    public Department update(@RequestBody Department department) {
        return departmentService.updateDepartment(department);
    }

    @DeleteMapping("/{id}")
   // @PreAuthorize("hasRole('ACADEMICIAN')")
    public void delete(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
    }
}