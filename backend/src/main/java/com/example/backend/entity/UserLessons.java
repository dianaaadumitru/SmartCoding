package com.example.backend.entity;

import com.example.backend.entity.embeddableIds.UserLessonId;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class UserLessons {
    @EmbeddedId
    private UserLessonId userLessonId;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("lessonId")
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    private Long courseId;

    @Column(columnDefinition = "boolean default false")
    private boolean completed;
}
