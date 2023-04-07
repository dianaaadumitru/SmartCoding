package com.example.backend.service;

import com.example.backend.dto.StudentDto;
import com.example.backend.entity.User;
import com.example.backend.exceptions.CrudOperationException;
import com.example.backend.repository.QuestionRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.UserResultsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final QuestionRepository questionRepository;

    private final UserResultsRepository userResultsRepository;

    public UserService(UserRepository userRepository, UserResultsRepository userResultsRepository, QuestionRepository questionRepository) {
        this.userRepository = userRepository;
        this.userResultsRepository = userResultsRepository;
        this.questionRepository = questionRepository;
    }

    public StudentDto addStudent(StudentDto studentDto) {
        User student = User.builder()
                .firstName(studentDto.getFirstName())
                .lastName(studentDto.getLastName())
                .username(studentDto.getUsername())
                .email(studentDto.getEmail())
                .password(studentDto.getPassword())
                .build();
        userRepository.save(student);
        studentDto.setStudentId(student.getUserId());
        return studentDto;
    }

    public void removeStudent(Long id) {
        userRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });
        userRepository.deleteById(id);
    }

    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        User student = userRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Student does not exist");
        });

        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setEmail(studentDto.getEmail());
        student.setPassword(studentDto.getPassword());
        student.setUsername(studentDto.getUsername());
        userRepository.save(student);
        studentDto.setStudentId(student.getUserId());
        return studentDto;
    }

    public StudentDto getStudentById(Long id) {
        User student = userRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Student does not exist");
        });

        return StudentDto.builder()
                .studentId(student.getUserId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .username(student.getUsername())
                .password(student.getPassword())
                .build();
    }

    public List<StudentDto> getAllStudents() {
        Iterable<User> students = userRepository.findAll();
        List<StudentDto> studentDtos = new ArrayList<>();

        students.forEach(student ->
                studentDtos.add(StudentDto.builder()
                        .studentId(student.getUserId())
                        .firstName(student.getFirstName())
                        .lastName(student.getLastName())
                        .email(student.getEmail())
                        .username(student.getUsername())
                        .password(student.getPassword())
                        .build())
        );
        return studentDtos;
    }

    public void addQuestionScoreToStudent(Long studentId, Long questionId, double score) {

    }
}
