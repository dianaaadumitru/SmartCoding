package com.example.backend.repository;

import com.example.backend.entity.embeddableIds.UserQuestionId;
import com.example.backend.entity.UserQuestionResults;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserResultsRepository extends CrudRepository<UserQuestionResults, UserQuestionId> {
    List<UserQuestionResults> findByUser_UserId(Long userId);
}
