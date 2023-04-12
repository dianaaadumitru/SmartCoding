package com.example.backend.controller;

import com.example.backend.service.CodeGeneratingService;
import com.example.backend.service.JupyterService;
import com.example.backend.websocket.RunRequestResult;
import com.example.backend.websocket.RunRequestResultIdDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/runCode")
public class JupyterController {
    private final JupyterService jupyterService;


    private final CodeGeneratingService codeGeneratingService;

    public JupyterController(JupyterService jupyterService, CodeGeneratingService codeGeneratingService) {
        this.jupyterService = jupyterService;
        this.codeGeneratingService = codeGeneratingService;
    }

    @GetMapping("/run")
    public ResponseEntity<RunRequestResultIdDto> sendRunRequest() {
        String code = codeGeneratingService.generateCode(5L);
        return ResponseEntity.ok(jupyterService.sendRunRequest(code));
    }


    @GetMapping("/run/{requestId}")
    public ResponseEntity<RunRequestResult> readRunRequestResult(@PathVariable int requestId) {
        return ResponseEntity.ok(jupyterService.readRunRequestResult(requestId));
    }
}
