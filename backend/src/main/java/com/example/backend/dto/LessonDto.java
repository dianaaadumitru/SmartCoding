package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonDto {
    private long id;

    private String name;

    private String description;

    private String longDescription;

    private String expectedTime;

    private int noLesson;
}
