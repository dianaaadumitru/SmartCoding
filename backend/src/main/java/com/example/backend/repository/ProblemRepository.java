package com.example.backend.repository;

import com.example.backend.entity.Problem;
import com.example.backend.entity.enums.Difficulty;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProblemRepository extends CrudRepository<Problem, Long> {
    @Query("SELECT DISTINCT p FROM Problem p WHERE p.difficulty IN :difficulties")
    List<Problem> findByDifficultyIn(List<Difficulty> difficulties);
}

