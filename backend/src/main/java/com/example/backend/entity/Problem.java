package com.example.backend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long problemId;

    private String name;

    private String description;

    private Difficulty difficulty;

    private String valuesType;

    private String valuesToCheckCode;

    private String resultsToCheckCode;

    private ReturnType returnType;

    @OneToMany(mappedBy = "problem")
    private List<UserProblemResults> userProblemResults;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
}
