package com.example.backend.client;

import com.example.backend.jupyter.model.CreateSessionJson;
import com.example.backend.jupyter.model.JupyterSessionDto;
import com.example.backend.jupyter.model.Kernel;
import com.example.backend.jupyter.model.SessionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.extern.java.Log;
import org.json.JSONObject;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


@Component
@Log
@Getter
public class JupyterClient {

    private final String host;

    private final Long port;

    private final String password;

    private final HttpClient client;

    private final String xsrf;

    private final String cookie;

    private final int webSocketTextMessageMaxSize;

    public JupyterClient(@Value("${jupyter.server.host}") String host, @Value("${jupyter.server.port}") Long port, @Value("${password}") String password, HttpClient httpClient, @Value("${webSocket.textMessage.maxSize:1000000}") int webSocketTextMessageMaxSize) {
        this.host = host;
        this.port = port;
        this.password = password;
        this.client = httpClient;
        this.webSocketTextMessageMaxSize = webSocketTextMessageMaxSize;
        xsrf = extractXsrf();
        cookie = extractCookie();
        cleanUpAllSessions();
    }

    public String extractXsrf() {
        try {
            URI uri = UriComponentsBuilder.fromUriString("http://" + host + ":" + port + "/login").build().toUri();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.headers().map().get("set-cookie").get(0).split(";")[0].substring(6);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Could not generate xsrf token" + e);
        }
    }

    public String extractCookie() {
        try {
            URI uri = UriComponentsBuilder.fromUriString("http://" + host + ":" + port + "/login")
                    .queryParam("_xsrf", xsrf)
                    .queryParam("password", password).build().toUri();

            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .setHeader("Cookie", "_xsrf=" + xsrf)
                    .uri(uri)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String cookieHeader = response.headers().map().get("set-cookie").toString().substring(1).split(";")[0];

            return "_xsrf=" + xsrf + "; " + cookieHeader;

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Could not generate cookie" + e);
        }
    }

    public JupyterSessionDto createSession() {
        try {
            URI uri = UriComponentsBuilder.fromUriString("http://" + host + ":" + port + "/api/sessions").queryParam("_xsrf", xsrf).build().toUri();

            var objectBody = CreateSessionJson.builder()
                    .kernel(Kernel.builder().name("Python3").build())
                    .type("notebook")
                    .path("")
                    .name("Untitled.ipynb")
                    .build();
            String body = new ObjectMapper().writeValueAsString(objectBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .setHeader("Cookie", cookie)
                    .uri(uri)
                    .build();

            String response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString()).body();

            JSONObject responseStringJson = new JSONObject(response);

            String sessionId = responseStringJson.getString("id");

            var kernelId = responseStringJson.getJSONObject("kernel").getString("id");

            return JupyterSessionDto.builder()
                    .sessionId(sessionId)
                    .kernelId(kernelId)
                    .build();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Could not generate cookie" + e);
        }
    }


    public int restartCode(String kernelId) {
        try {
            URI uri = UriComponentsBuilder.fromUriString("http://" + host + ":" + port + "/api/kernels/" + kernelId + "/restart").queryParam("_xsrf", xsrf).build().toUri();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .setHeader("Cookie", cookie)
                    .uri(uri)
                    .build();

            return client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString()).statusCode();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Could not restart kernel with id " + kernelId, e);
        }
    }

    public void cleanupSession(String sessionId) {
        try {
            URI uri = UriComponentsBuilder.fromUriString("http://" + host + ":" + port + "/api/sessions/" + sessionId).queryParam("_xsrf", xsrf).build().toUri();
            HttpRequest request = HttpRequest.newBuilder()
                    .DELETE()
                    .setHeader("Cookie", cookie)
                    .uri(uri)
                    .build();

            client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            log.log(Level.SEVERE, "Could not delete jupyter session with id: " + sessionId + ".", e);
        }
    }

    public List<String> getActiveSessionIds() {
        try {
            URI uri = UriComponentsBuilder.fromUriString("http://" + host + ":" + port + "/api/sessions").queryParam("_xsrf", xsrf).build().toUri();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .setHeader("Cookie", cookie)
                    .uri(uri)
                    .build();

            String responseString = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString()).body();

            Type listType = new TypeToken<ArrayList<SessionResponse>>() {
            }.getType();
            ArrayList<SessionResponse> sessions = new Gson().fromJson(responseString, listType);
            return sessions.stream().map(SessionResponse::getId).toList();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("could not get all active sessions " + e);
        }
    }

    public void cleanUpAllSessions() {
        List<String> sessions = getActiveSessionIds();
        for (String id : sessions) {
            cleanupSession(id);
        }
    }


}
