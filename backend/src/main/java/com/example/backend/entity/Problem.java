package com.example.backend.entity;

import com.example.backend.entity.enums.Difficulty;
import com.example.backend.entity.enums.ReturnType;
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

    @Column(length = 10000)
    private String description;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    private String valuesType;

    private String valuesToCheckCode;

    private String resultsToCheckCode;

    @Enumerated(EnumType.STRING)
    private ReturnType returnType;

    @OneToMany(mappedBy = "problem")
    private List<UserProblemResults> userProblemResults;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
}
