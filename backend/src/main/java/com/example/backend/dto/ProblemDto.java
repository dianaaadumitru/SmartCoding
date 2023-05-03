package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemDto {
    private long problemId;

    private String description;

    private String difficulty;

    private String valuesType;

    private String valuesToCheckCode;

    private String resultsToCheckCode;
}
