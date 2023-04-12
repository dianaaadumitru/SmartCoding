package com.example.backend.repository;

import com.example.backend.entity.UserQuestionId;
import com.example.backend.entity.UserResults;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserResultsRepository extends CrudRepository<UserResults, UserQuestionId> {
    List<UserResults> findByUser_UserId(Long userId);
}
