package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long studentId;

    private String name;

    private String username;

    private String email;

    private String password;

    @Column(columnDefinition = "float default 0")
    private double testResult;
}
