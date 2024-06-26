package com.example.backend.repository;

import com.example.backend.entity.UserProblemResults;
import com.example.backend.entity.embeddableIds.UserProblemId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserProblemResultsRepository extends CrudRepository<UserProblemResults, UserProblemId> {
    List<UserProblemResults> findByUser_UserId(Long userId);

    List<UserProblemResults> findByProblem_ProblemId(Long userId);

}
