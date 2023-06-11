package com.example.backend.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class ResultDto {
    private Double finalResult;
    private String printedResult;
    private String pythonCodeStatus;
}
