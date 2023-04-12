package com.example.backend.websocket;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CodeExecutionResult {
    private String printedResult;

    private String returnedResult;

    private String pythonCodeStatus;
}
