package com.example.backend.websocket.handler;


import com.example.backend.websocket.CodeExecutionResult;
import com.example.backend.websocket.WebSocketResponseJsonDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@AllArgsConstructor
public class MessageTypeExecuteReplyHandler implements Consumer<WebSocketResponseJsonDto> {

    private final CodeExecutionResult codeExecutionResult;
    private Map<String, Consumer<WebSocketSession>> statusConditionalOperations;

    private WebSocketSession webSocketSession;

    @Override
    public void accept(WebSocketResponseJsonDto webSocketResponseJsonDto) {
        String status = webSocketResponseJsonDto.getContent().getStatus();
        statusConditionalOperations.getOrDefault(status, (e) -> {
        }).accept(webSocketSession);
        codeExecutionResult.setPythonCodeStatus(status);
    }
}
