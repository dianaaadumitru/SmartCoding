package com.example.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CodeValueToCompileDto {
    private String code;

    private String valuesType;

    private String valuesToCheckCode;

    private String resultsToCheckCode;

    private String returnType;
}
