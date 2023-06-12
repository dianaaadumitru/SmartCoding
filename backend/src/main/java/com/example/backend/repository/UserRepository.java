package com.example.backend.repository;

import com.example.backend.entity.Course;
import com.example.backend.entity.Problem;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("SELECT c, COUNT(c.courseId) as courseCount " +
            "FROM User u JOIN u.courses c " +
            "GROUP BY c " +
            "ORDER BY courseCount DESC ")
    List<Course> findTop8Courses();

    @Query("SELECT p, COUNT(upr.userProblemId.problemId) as problemCount " +
            "FROM UserProblemResults upr " +
            "JOIN upr.problem p " +
            "GROUP BY p " +
            "ORDER BY problemCount DESC")
    List<Problem> findTop8Problems();
}
