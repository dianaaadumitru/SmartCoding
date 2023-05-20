package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonDto {
    private long id;

    private String name;

    private String description;

    private String expectedTime;

    @Column(columnDefinition = "integer default 0")
    private int noLesson;
}
