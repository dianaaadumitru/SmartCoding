package com.example.backend.websocket.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.function.Consumer;

@Slf4j
public class StatusOkHandler implements Consumer<WebSocketSession> {

    @Override
    public void accept(WebSocketSession webSocketSession) {
        try {
            log.info("python successful - " + webSocketSession);
            webSocketSession.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
