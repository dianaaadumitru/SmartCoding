package com.example.backend.controller;

import com.example.backend.dto.StudentDto;
import com.example.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final UserService userService;

    public StudentController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        return ResponseEntity.ok(userService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getStudentById(id));
    }

    @PostMapping
    public ResponseEntity<StudentDto> addStudent(@RequestBody StudentDto studentDto) {
        return ResponseEntity.ok(userService.addStudent(studentDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id, @RequestBody StudentDto studentDto) {
        return ResponseEntity.ok(userService.updateStudent(id, studentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeStudent(@PathVariable Long id) {
        userService.removeStudent(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
