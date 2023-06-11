package com.example.backend.repository;

import com.example.backend.entity.Course;
import com.example.backend.entity.enums.Difficulty;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseRepository extends CrudRepository<Course, Long> {
    @Query("SELECT DISTINCT c FROM Course c JOIN c.courseTypes ct WHERE ct.type LIKE %:keyword%")
    List<Course> findByCourseTypeKeyword(String keyword);
    @Query("SELECT DISTINCT c FROM Course c WHERE c.difficulty IN :difficulties")
    List<Course> findByDifficultyIn(List<Difficulty> difficulties);

    List<Course> findByNameContaining(String name);
}
