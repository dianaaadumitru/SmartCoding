package com.example.backend.websocket;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Builder
@Slf4j
@Setter
public class CodeWebSocketHandler extends TextWebSocketHandler {

    private final CopyOnWriteArrayList<RunRequestResult> runRequestResultCopyOnWriteArrayList;
    private final int requestId;
    private final CodeExecutionResult codeExecutionResult;
    private Map<String, Consumer<WebSocketResponseJsonDto>> messageTypeConditionalOperations;

    private Runnable cleanUpCode;

    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        WebSocketResponseJsonDto webSocketResponseJsonDto = new Gson().fromJson(payload, WebSocketResponseJsonDto.class);
        String messageType = webSocketResponseJsonDto.getMsg_type();

        log.info("payload: " + payload);
        log.info("message type: " + messageType);

        messageTypeConditionalOperations.getOrDefault(messageType, (a) -> {
        }).accept(webSocketResponseJsonDto);
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        log.info("established connection - " + session);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) {
        RunRequestResult runRequestResult = runRequestResultCopyOnWriteArrayList.get(requestId);
        runRequestResult.setRequestStatus(RunRequestResult.RequestStatusLevel.DONE);
        runRequestResult.setCodeExecutionResult(codeExecutionResult);
        cleanUpCode.run();
        log.info("closed connection - " + session);
        log.info("close status - " + closeStatus);
    }
}