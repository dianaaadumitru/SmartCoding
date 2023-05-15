package com.example.backend.repository;

import com.example.backend.entity.Course;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Long> {
//    List<Course> getAllByCourseTypesContains(Set<CourseType> courseTypes);
}
