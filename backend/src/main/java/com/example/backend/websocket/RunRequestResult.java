package com.example.backend.websocket;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RunRequestResult {
    private RequestStatusLevel requestStatus;
    private CodeExecutionResult codeExecutionResult;

    public RunRequestResult(RequestStatusLevel requestStatus) {
        this.requestStatus = requestStatus;
    }

    public enum RequestStatusLevel {
        NOT_FOUND,
        PENDING,
        DONE
    }
}