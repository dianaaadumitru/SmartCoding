package com.example.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class UserDto {
    private long userId;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String password;

    private String userType;

    private double testResult;
}
