package com.example.backend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long lessonId;

    private String name;

    private String description;

    private String expectedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "lesson", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Problem> problems;

//    private int noLesson;
}
