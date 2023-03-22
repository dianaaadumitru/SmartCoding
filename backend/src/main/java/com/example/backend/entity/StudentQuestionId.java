package com.example.backend.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentQuestionId implements Serializable {

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "question_id")
    private Long questionId;

}
