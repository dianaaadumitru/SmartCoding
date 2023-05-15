package com.example.backend.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class ResultDto { //todo: add here the error string and check if the code returns an error and handle it
    private double finalResult;
}
