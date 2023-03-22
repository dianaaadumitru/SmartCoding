package com.example.backend.service;

import com.example.backend.dto.StudentDto;
import com.example.backend.entity.Student;
import com.example.backend.exceptions.CrudOperationException;
import com.example.backend.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public StudentDto addStudent(StudentDto studentDto) {
        Student student = Student.builder()
                .name(studentDto.getName())
                .username(studentDto.getUsername())
                .email(studentDto.getEmail())
                .password(studentDto.getPassword())
                .build();
        studentRepository.save(student);
        studentDto.setStudentId(student.getStudentId());
        return studentDto;
    }

    public void removeStudent(Long id) {
        studentRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Student does not exist");
        });

        studentRepository.deleteById(id);
    }

    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        Student student = studentRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Student does not exist");
        });

        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());
        student.setPassword(studentDto.getPassword());
        student.setUsername(studentDto.getUsername());
        studentRepository.save(student);
        studentDto.setStudentId(student.getStudentId());
        return studentDto;
    }

    public StudentDto getStudentById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Student does not exist");
        });

        return StudentDto.builder()
                .studentId(student.getStudentId())
                .name(student.getName())
                .email(student.getEmail())
                .username(student.getUsername())
                .password(student.getPassword())
                .build();
    }

    public List<StudentDto> getAllStudents() {
        Iterable<Student> students = studentRepository.findAll();
        List<StudentDto> studentDtos = new ArrayList<>();

        students.forEach(student ->
                studentDtos.add(StudentDto.builder()
                        .studentId(student.getStudentId())
                        .name(student.getName())
                        .email(student.getEmail())
                        .username(student.getUsername())
                        .password(student.getPassword())
                        .build())
        );
        return studentDtos;
    }
}
