package com.example.backend.service;

import com.example.backend.dto.StudentDto;
import com.example.backend.entity.User;
import com.example.backend.exceptions.CrudOperationException;
import com.example.backend.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final StudentRepository studentRepository;

    public UserService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public StudentDto addStudent(StudentDto studentDto) {
        User user = User.builder()
                .name(studentDto.getName())
                .username(studentDto.getUsername())
                .email(studentDto.getEmail())
                .password(studentDto.getPassword())
                .build();
        studentRepository.save(user);
        studentDto.setStudentId(user.getStudentId());
        return studentDto;
    }

    public void removeStudent(Long id) {
        studentRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });

        studentRepository.deleteById(id);
    }

    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        User user = studentRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });

        user.setName(studentDto.getName());
        user.setEmail(studentDto.getEmail());
        user.setPassword(studentDto.getPassword());
        user.setUsername(studentDto.getUsername());
        studentRepository.save(user);
        studentDto.setStudentId(user.getStudentId());
        return studentDto;
    }

    public StudentDto getStudentById(Long id) {
        User user = studentRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });

        return StudentDto.builder()
                .studentId(user.getStudentId())
                .name(user.getName())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

    public List<StudentDto> getAllStudents() {
        Iterable<User> students = studentRepository.findAll();
        List<StudentDto> studentDtos = new ArrayList<>();

        students.forEach(user ->
                studentDtos.add(StudentDto.builder()
                        .studentId(user.getStudentId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .build())
        );
        return studentDtos;
    }
}
