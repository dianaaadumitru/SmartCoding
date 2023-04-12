package com.example.backend.websocket.handler;

import com.example.backend.websocket.CodeExecutionResult;
import com.example.backend.websocket.WebSocketResponseJsonDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
@AllArgsConstructor
public class MessageTypeExecuteResultHandler implements Consumer<WebSocketResponseJsonDto> {

    private final CodeExecutionResult codeExecutionResult;

    @Override
    public void accept(WebSocketResponseJsonDto webSocketResponseJsonDto) {
        String returnedResult = webSocketResponseJsonDto.getContent().getData().get("text/plain");
        log.info("received output from code - " + returnedResult);
        codeExecutionResult.setReturnedResult(returnedResult);
    }
}
