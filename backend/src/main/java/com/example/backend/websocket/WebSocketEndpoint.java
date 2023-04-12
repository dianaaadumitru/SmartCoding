package com.example.backend.websocket;

import lombok.Builder;
import lombok.Getter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Builder
@Getter
public class WebSocketEndpoint {

    private static final String GENERAL_URI = "ws://%s:%s/api/kernels/%s/channels?session_id=%s&_xsrf=%s";

    private String host;

    private Long port;

    private String kernelId;

    private String sessionId;

    private String xsrf;

    public String getUriString() {
        return String.format(GENERAL_URI,
                host,
                port,
                encode(kernelId),
                encode(sessionId),
                encode(xsrf));
    }

    private String encode(String text) {
        return URLEncoder.encode(text, StandardCharsets.UTF_8);
    }
}