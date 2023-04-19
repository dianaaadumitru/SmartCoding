package com.example.backend.service;

import com.example.backend.client.JupyterClient;
import com.example.backend.jupyter.model.JupyterSessionDto;
import com.example.backend.websocket.RunRequestResult;
import com.example.backend.websocket.RunRequestResultIdDto;
import com.example.backend.websocket.WebSocketEndpoint;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JupyterService {

    private final JupyterClient jupyterClient;

    public JupyterService(JupyterClient jupyterClient) {
        this.jupyterClient = jupyterClient;
    }

    public List<String> getActiveSessions() {
        return jupyterClient.getActiveSessionIds();
    }

    public String getXsrf() {
        return jupyterClient.extractXsrf();
    }

    public String getCookie() {
        return jupyterClient.extractCookie();
    }

    public JupyterSessionDto getCreatedSession() {
        return jupyterClient.createSession();
    }

//    public int restartCodeStatusCode() {
//        return jupyterClient.restartCode();
//    }

    public RunRequestResultIdDto sendRunRequest(String code) {
        String xsrf = jupyterClient.getXsrf();
        String cookie = jupyterClient.getCookie();

        JupyterSessionDto sessionDto = jupyterClient.createSession();
        jupyterClient.restartCode(sessionDto.getKernelId());

        WebSocketEndpoint webSocketEndpoint = WebSocketEndpoint.builder()
                .host(jupyterClient.getHost())
                .port(jupyterClient.getPort())
                .kernelId(sessionDto.getKernelId())
                .sessionId(sessionDto.getSessionId())
                .xsrf(xsrf)
                .build();

        return new RunRequestResultIdDto(jupyterClient.runCode(webSocketEndpoint, cookie, code));

    }

    public RunRequestResult readRunRequestResult(int id) {
        try {
            RunRequestResult runRequestResult = jupyterClient.getRunRequestResultCopyOnWriteArrayList().get(id);
            if (runRequestResult.getRequestStatus() == RunRequestResult.RequestStatusLevel.DONE) {
                jupyterClient.getRunRequestResultCopyOnWriteArrayList().set(id, new RunRequestResult(RunRequestResult.RequestStatusLevel.NOT_FOUND));
            }
            return runRequestResult;
        } catch (IndexOutOfBoundsException e) {
            return new RunRequestResult(RunRequestResult.RequestStatusLevel.NOT_FOUND);
        }
    }

}
