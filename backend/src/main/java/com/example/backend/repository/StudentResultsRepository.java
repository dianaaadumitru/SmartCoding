package com.example.backend.repository;

import com.example.backend.entity.StudentQuestionId;
import com.example.backend.entity.StudentResults;
import org.springframework.data.repository.CrudRepository;

public interface StudentResultsRepository extends CrudRepository<StudentResults, StudentQuestionId> {
}
