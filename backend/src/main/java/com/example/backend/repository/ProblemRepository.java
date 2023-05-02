package com.example.backend.repository;

import com.example.backend.entity.Problem;
import org.springframework.data.repository.CrudRepository;

public interface ProblemRepository extends CrudRepository<Problem, Long> {
}

