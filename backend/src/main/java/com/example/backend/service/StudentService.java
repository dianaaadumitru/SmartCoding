package com.example.backend.service;

import com.example.backend.dto.StudentDto;
import com.example.backend.entity.Student;
import com.example.backend.exceptions.CrudOperationException;
import com.example.backend.repository.QuestionRepository;
import com.example.backend.repository.StudentRepository;
import com.example.backend.repository.StudentResultsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    private final StudentResultsRepository studentResultsRepository;

    private final QuestionRepository questionRepository;

    public StudentService(StudentRepository studentRepository, StudentResultsRepository studentResultsRepository, QuestionRepository questionRepository) {
        this.studentRepository = studentRepository;
        this.studentResultsRepository = studentResultsRepository;
        this.questionRepository = questionRepository;
    }

    public StudentDto addStudent(StudentDto studentDto) {
        Student student = Student.builder()
                .firstName(studentDto.getFirstName())
                .lastName(studentDto.getLastName())
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

        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
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
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
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
