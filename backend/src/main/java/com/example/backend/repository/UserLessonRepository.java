package com.example.backend.repository;

import com.example.backend.entity.UserLessons;
import com.example.backend.entity.embeddableIds.UserLessonId;
import org.springframework.data.repository.CrudRepository;

public interface UserLessonRepository extends CrudRepository<UserLessons, UserLessonId> {
}
