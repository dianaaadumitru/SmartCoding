package com.example.backend.controller;

import com.example.backend.dto.CodeValueToCompileDto;
import com.example.backend.dto.ResultDto;
import com.example.backend.jupyter.model.JupyterSessionDto;
import com.example.backend.service.CodeGeneratingService;
import com.example.backend.service.JupyterService;
import com.example.backend.websocket.RunRequestResult;
import com.example.backend.websocket.RunRequestResultIdDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/runCode")
@Slf4j
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

    @GetMapping("/run/results")
    public ResponseEntity<ResultDto> readFinalResult(@RequestBody CodeValueToCompileDto data) throws ExecutionException, InterruptedException {
        int successes = 0;
        String[] valuesToCheck = data.getValuesToCheckCode().split(",");
        String[] resultsToCheck = data.getResultsToCheckCode().split(",");
        for (int i = 0; i < valuesToCheck.length; i++) {
            String currentValue = "";
            if (Objects.equals(data.getValuesType(), "String")) {
                currentValue = "\"" + valuesToCheck[i].strip() + "\"";
            } else if (Objects.equals(data.getValuesType(), "Integer")) {
                currentValue = valuesToCheck[i].strip();
            }
            System.out.println("value that is checked: " + currentValue + " having result: " + resultsToCheck[i]);
            String codeToCompile = codeGeneratingService.generateCode(data.getCode(), currentValue);
            log.info("\n" + codeToCompile);

            ExecutorService threadpool = Executors.newCachedThreadPool();
            Future<RunRequestResultIdDto> futureTask = threadpool.submit(() -> jupyterService.sendRunRequest(codeToCompile));

            while (!futureTask.isDone()) {

            }
            RunRequestResultIdDto runRequestResultId = futureTask.get();

            threadpool.shutdown();

            RunRequestResult result = jupyterService.readRunRequestResult(runRequestResultId.getRequestId());
            while (result.getRequestStatus() == RunRequestResult.RequestStatusLevel.PENDING) {
                result = jupyterService.readRunRequestResult(runRequestResultId.getRequestId());
                if (result.getRequestStatus() == RunRequestResult.RequestStatusLevel.PENDING) {
                    try {
                        Thread.sleep(1000); // Wait for 1 second before calling the method again
                    } catch (InterruptedException e) {

                    }
                }
                System.out.println(result + " " + resultsToCheck[i]);
            }
            System.out.println(result.getCodeExecutionResult().getReturnedResult());
            if (Objects.equals(result.getCodeExecutionResult().getReturnedResult(), resultsToCheck[i].strip())) {
                successes++;
            }
        }
        System.out.println(successes + " " + valuesToCheck.length + " " + ((double) successes / valuesToCheck.length));
        return ResponseEntity.ok(ResultDto.builder().finalResult(((double) successes / valuesToCheck.length) * 100).build());
    }
}
