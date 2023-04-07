package com.example.backend.repository;

import com.example.backend.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<User, Long> {
}
