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
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long problemId;

    private String description;

    private String difficulty;

    private String valuesType;

    private String valuesToCheckCode;

    private String resultsToCheckCode;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserResults> userResults;
}
