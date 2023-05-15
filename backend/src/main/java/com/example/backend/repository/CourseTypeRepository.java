package com.example.backend.repository;

import com.example.backend.entity.CourseType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CourseTypeRepository extends CrudRepository<CourseType, Long> {
    Optional<CourseType> findByType(String type);
}
