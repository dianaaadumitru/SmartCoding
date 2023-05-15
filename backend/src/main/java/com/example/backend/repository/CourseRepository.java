package com.example.backend.repository;

import com.example.backend.entity.Course;
import com.example.backend.entity.CourseType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface CourseRepository extends CrudRepository<Course, Long> {
//    List<Course> getAllByCourseTypesContains(Set<CourseType> courseTypes);
}
