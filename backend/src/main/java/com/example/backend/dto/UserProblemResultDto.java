package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProblemResultDto {
    private Long userId;

    private Long problemId;

    private String problemName;

    private String problemDescription;

    private String problemDifficulty;

    private double percentage;

    private String answer;
}
