package com.example.demo.controller;

import com.example.demo.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/management/api/v1/students")
public class StudentManagementController {

    private final List<Student> STUDENTS = Arrays.asList(
            new Student(1, "James Bond"),
            new Student(2, "Maria Jones"),
            new Student(3, "Anna Smith")
    );

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINTRAINEE')")
    public List<Student> getAllStudents() {
        log.info("GET all students");
        return STUDENTS;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('course:write')")
    public Student registerNewStudent(@RequestBody Student student) {
        log.info("POST new student");
        return student;
    }

    @DeleteMapping(path = "{studentId}")
    @PreAuthorize("hasAuthority('course:write')")
    public void deleteStudent(@PathVariable("studentId") Integer studentId) {
        log.info("DELETE student by id = {}", studentId);
        System.out.println("Deleted student " + studentId);
    }

    @PutMapping(path = "{studentId}")
    @PreAuthorize("hasAuthority('course:write')")
    public void updateStudent(@PathVariable("studentId") Integer studentId, @RequestBody Student student) {
        log.info("UPDATE student id = {} info with {}", studentId, student);
        System.out.printf("%s %s%n", studentId, student);
    }
}
