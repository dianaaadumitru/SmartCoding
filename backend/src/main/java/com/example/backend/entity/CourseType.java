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
public class CourseType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long courseTypeId;

    private String type;


    @ManyToMany(mappedBy = "courseTypes")
    private List<Course> courses;
}
