package com.example.backend.controller;

import com.example.backend.dto.CodeValueToCompileDto;
import com.example.backend.dto.ResultDto;
import com.example.backend.jupyter.model.JupyterSessionDto;
import com.example.backend.service.CodeGeneratingService;
import com.example.backend.service.JupyterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/runCode")
@Slf4j
public class JupyterController {
    private final JupyterService jupyterService;


    public JupyterController(JupyterService jupyterService, CodeGeneratingService codeGeneratingService) {
        this.jupyterService = jupyterService;
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

//    @GetMapping("/run")
//    public ResponseEntity<RunRequestResultIdDto> sendRunRequest(@RequestBody CodeValueToCompileDto data) {
//        String codeToCompile = codeGeneratingService.generateCode(data.getCode(), data.getValue());
//        log.info("\n" + codeToCompile);
//        return ResponseEntity.ok(jupyterService.sendRunRequest(codeToCompile));
//    }
//
//
//    @GetMapping("/run/{requestId}")
//    public ResponseEntity<RunRequestResult> readRunRequestResult(@PathVariable int requestId) {
//        return ResponseEntity.ok(jupyterService.readRunRequestResult(requestId));
//    }

    @PostMapping("/run/results")
    public ResponseEntity<ResultDto> readFinalResult(@RequestBody CodeValueToCompileDto data) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(jupyterService.readFinalResult(data));
    }
}
