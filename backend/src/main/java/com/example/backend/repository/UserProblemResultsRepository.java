package com.example.backend.repository;

import com.example.backend.entity.UserProblemId;
import com.example.backend.entity.UserProblemResults;
import org.springframework.data.repository.CrudRepository;

public interface UserProblemResultsRepository extends CrudRepository<UserProblemResults, UserProblemId> {
}
