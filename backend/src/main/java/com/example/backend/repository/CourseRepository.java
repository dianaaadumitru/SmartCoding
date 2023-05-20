package com.example.backend.repository;

import com.example.backend.entity.Course;
import com.example.backend.entity.Difficulty;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseRepository extends CrudRepository<Course, Long> {
    //    List<Course> getAllByCourseTypesContains(Set<CourseType> courseTypes);
    @Query("SELECT DISTINCT c FROM Course c WHERE c.difficulty IN :difficulties")
    List<Course> findByDifficultyIn(List<Difficulty> difficulties);
}
