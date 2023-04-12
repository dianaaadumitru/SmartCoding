package com.example.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UserAndFinalScoreDto {
    private Long userId;

    private String firstName;

    private String lastName;

    private double testResult;
}
