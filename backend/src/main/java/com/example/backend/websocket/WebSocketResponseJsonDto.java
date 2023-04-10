package com.example.backend.websocket;

import lombok.Getter;

import java.util.Map;

@Getter
public class WebSocketResponseJsonDto {

    private String msg_type;
    private WebSocketResponseJsonDtoContent content;

    @Getter
    public static class WebSocketResponseJsonDtoContent {
        private String status;
        private Map<String, String> data;
        private String text;
    }
}