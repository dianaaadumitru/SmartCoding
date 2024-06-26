package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDto {
    private long questionId;

    private long quizId;

    private String description;

    private double score;

    private String valuesType;

    private String valuesToCheckCode;

    private String resultsToCheckCode;
}
