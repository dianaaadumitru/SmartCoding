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
public class UserQuestionId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "question_id")
    private Long questionId;

}
