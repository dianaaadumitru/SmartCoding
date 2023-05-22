package com.example.backend.entity;

import com.example.backend.entity.enums.Difficulty;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long courseId;

    private String name;

    private String description;

    private Difficulty difficulty;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "course_courseTypes",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "courseType_id"))
    private Set<CourseType> courseTypes;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Lesson> lessons;

    @ManyToMany(mappedBy = "courses")
    private Set<User> users;
}
