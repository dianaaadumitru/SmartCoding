package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDto {
    private long studentId;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String password;

    private double testResult;
}
