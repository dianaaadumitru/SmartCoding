package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTestResultDto {
    private Long userId;

    private String userFirstName;

    private String userLastName;

    private double testResult;
}
