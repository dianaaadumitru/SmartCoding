package com.example.backend.websocket.handler;

import com.example.backend.websocket.CodeExecutionResult;
import com.example.backend.websocket.WebSocketResponseJsonDto;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
public class MessageTypeStreamHandler implements Consumer<WebSocketResponseJsonDto> {

    private final CodeExecutionResult codeExecutionResult;


    public MessageTypeStreamHandler(CodeExecutionResult codeExecutionResult) {
        this.codeExecutionResult = codeExecutionResult;
    }

    public void accept(WebSocketResponseJsonDto webSocketResponseJsonDto) {
        String printedResult = webSocketResponseJsonDto.getContent().getText();
        log.info("received printed code - " + printedResult);
        codeExecutionResult.setPrintedResult(printedResult);
    }
}