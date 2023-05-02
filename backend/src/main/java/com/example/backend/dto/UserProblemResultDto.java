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

    private String userFirstName;

    private String userLastName;

    private Long problemId;

    private String problemDescription;

    private int percentage;

    private String answer;
}
