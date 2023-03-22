package com.example.backend.service;

import com.example.backend.dto.TeacherDto;
import com.example.backend.entity.Teacher;
import com.example.backend.exceptions.CrudOperationException;
import com.example.backend.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public TeacherDto addTeacher(TeacherDto teacherDto) {
        Teacher teacher = Teacher.builder()
                .name(teacherDto.getName())
                .username(teacherDto.getUsername())
                .email(teacherDto.getEmail())
                .password(teacherDto.getPassword())
                .build();
        teacherRepository.save(teacher);
        teacherDto.setTeacherId(teacher.getTeacherId());
        return teacherDto;
    }

    public void removeTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Teacher does not exist");
        });

        teacherRepository.deleteById(id);
    }

    public TeacherDto updateTeacher(Long id, TeacherDto teacherDto) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Teacher does not exist");
        });

        teacher.setName(teacherDto.getName());
        teacher.setEmail(teacherDto.getEmail());
        teacher.setPassword(teacherDto.getPassword());
        teacher.setUsername(teacherDto.getUsername());
        teacherRepository.save(teacher);
        teacherDto.setTeacherId(teacher.getTeacherId());

        return teacherDto;
    }

    public TeacherDto getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Teacher does not exist");
        });

        return TeacherDto.builder()
                .name(teacher.getName())
                .email(teacher.getEmail())
                .username(teacher.getUsername())
                .password(teacher.getPassword())
                .build();
    }

    public List<TeacherDto> getAllTeachers() {
        Iterable<Teacher> teachers = teacherRepository.findAll();
        List<TeacherDto> teacherDtos = new ArrayList<>();

        teachers.forEach(teacher ->
                teacherDtos.add(TeacherDto.builder()
                        .teacherId(teacher.getTeacherId())
                        .name(teacher.getName())
                        .email(teacher.getEmail())
                        .username(teacher.getUsername())
                        .password(teacher.getPassword())
                        .build())
        );
        return teacherDtos;
    }
}
