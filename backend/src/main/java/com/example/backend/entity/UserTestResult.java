package com.example.backend.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserTestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long studentTestResultId;

    @OneToOne(mappedBy = "userTestResult")
    private User user;

    @Column(columnDefinition = "float default 0")
    private double testResult;
}
