package com.example.backend.entity;

import com.example.backend.entity.embeddableIds.UserProblemId;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class UserProblemResults {

    @EmbeddedId
    private UserProblemId userProblemId;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("problemId")
    @JoinColumn(name = "problem_id")
    private Problem problem;

    private double percentage;

    @Column(length = 10000)
    private String answer;

}
