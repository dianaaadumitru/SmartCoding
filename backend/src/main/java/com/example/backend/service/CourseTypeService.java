package com.example.backend.service;

import com.example.backend.dto.CourseTypeDto;
import com.example.backend.entity.CourseType;
import com.example.backend.exceptions.CrudOperationException;
import com.example.backend.repository.CourseTypeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseTypeService {
    private final CourseTypeRepository courseTypeRepository;

    public CourseTypeService(CourseTypeRepository courseTypeRepository) {
        this.courseTypeRepository = courseTypeRepository;
    }

    public CourseTypeDto addCourseType(CourseTypeDto courseTypeDto) {
        CourseType courseType = CourseType.builder()
                .type(courseTypeDto.getType())
                .build();
        courseTypeRepository.save(courseType);
        courseTypeDto.setId(courseType.getCourseTypeId());
        return courseTypeDto;
    }

    public void removeCourseType(Long id) {
        courseTypeRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("CourseType does not exist!");
        });
        courseTypeRepository.deleteById(id);
    }

    public CourseTypeDto updateCourseType(Long id, CourseTypeDto courseTypeDto) {
        CourseType courseType = courseTypeRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("CourseType does not exist!");
        });

        courseType.setType(courseTypeDto.getType());
        courseTypeRepository.save(courseType);

        courseTypeDto.setId(courseType.getCourseTypeId());
        return courseTypeDto;
    }

    public CourseTypeDto getCourseTypeById(Long id) {
        CourseType courseType = courseTypeRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("CourseType does not exist!");
        });

        return CourseTypeDto.builder()
                .id(courseType.getCourseTypeId())
                .type(courseType.getType())
                .build();
    }

    public List<CourseTypeDto> getAllCourseTypes() {
        Iterable<CourseType> courseTypes = courseTypeRepository.findAll();
        List<CourseTypeDto> courseTypeDtos = new ArrayList<>();

        courseTypes.forEach(courseType ->
                courseTypeDtos.add(CourseTypeDto.builder()
                        .id(courseType.getCourseTypeId())
                        .type(courseType.getType())
                        .build()));
        return courseTypeDtos;
    }

    public CourseTypeDto findByCourseType(String givenCourseTypeName) {
        CourseType courseType = courseTypeRepository.findByType(givenCourseTypeName).orElseThrow(() -> {
            throw new CrudOperationException("CourseType does not exist!");
        });

        return CourseTypeDto.builder()
                .id(courseType.getCourseTypeId())
                .type(courseType.getType())
                .build();
    }
}
