package com.example.backend.repository;

import com.example.backend.entity.UserQuestionId;
import com.example.backend.entity.UserResults;
import org.springframework.data.repository.CrudRepository;

public interface StudentResultsRepository extends CrudRepository<UserResults, UserQuestionId> {
}
