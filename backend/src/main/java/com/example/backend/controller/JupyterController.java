package com.example.backend.controller;

import com.example.backend.jupyter.model.JupyterSessionDto;
import com.example.backend.service.CodeGeneratingService;
import com.example.backend.service.JupyterService;
import com.example.backend.websocket.RunRequestResult;
import com.example.backend.websocket.RunRequestResultIdDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/runCode")
public class JupyterController {
    private final JupyterService jupyterService;


    private final CodeGeneratingService codeGeneratingService;

    public JupyterController(JupyterService jupyterService, CodeGeneratingService codeGeneratingService) {
        this.jupyterService = jupyterService;
        this.codeGeneratingService = codeGeneratingService;
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<String>> getAllActiveSessions() {
        return ResponseEntity.ok(jupyterService.getActiveSessions());
    }

    @GetMapping("/xsrf")
    public ResponseEntity<String> getXsrf() {
        return ResponseEntity.ok(jupyterService.getXsrf());
    }

    @GetMapping("/cookie")
    public ResponseEntity<String> getCookie() {
        return ResponseEntity.ok(jupyterService.getCookie());
    }

    @GetMapping("/newSession")
    public ResponseEntity<JupyterSessionDto> getCreatedSession() {
        return ResponseEntity.ok(jupyterService.getCreatedSession());
    }

    @GetMapping("/run")
    public ResponseEntity<RunRequestResultIdDto> sendRunRequest(@RequestBody String code) {
//        String code = codeGeneratingService.generateCode(5L);
        return ResponseEntity.ok(jupyterService.sendRunRequest(code));
    }


    @GetMapping("/run/{requestId}")
    public ResponseEntity<RunRequestResult> readRunRequestResult(@PathVariable int requestId) {
        return ResponseEntity.ok(jupyterService.readRunRequestResult(requestId));
    }
}
