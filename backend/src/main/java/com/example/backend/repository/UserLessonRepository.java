package com.example.backend.repository;

import com.example.backend.entity.User;
import com.example.backend.entity.UserLessons;
import com.example.backend.entity.embeddableIds.UserLessonId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserLessonRepository extends CrudRepository<UserLessons, UserLessonId> {
    Optional<UserLessons> findByUserLessonIdAndCourseId(UserLessonId userLessonId, Long courseId);

    List<UserLessons> findAllByCourseId(Long courseId);

    List<UserLessons> findAllByUserAndCourseId(User user, Long courseId);
}
