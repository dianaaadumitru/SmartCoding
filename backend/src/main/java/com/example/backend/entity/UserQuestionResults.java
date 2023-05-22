package com.example.backend.entity;

import com.example.backend.entity.embeddableIds.UserQuestionId;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserQuestionResults {
    @EmbeddedId
    private UserQuestionId userQuestionId;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("questionId")
    @JoinColumn(name = "question_id")
    private Question question;

    private double score;

    private String answer;
}
