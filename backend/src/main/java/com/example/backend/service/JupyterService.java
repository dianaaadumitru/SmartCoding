package com.example.backend.service;

import com.example.backend.client.JupyterClient;
import com.example.backend.dto.CodeValueToCompileDto;
import com.example.backend.dto.ResultDto;
import com.example.backend.jupyter.model.JupyterSessionDto;
import com.example.backend.websocket.RunRequestResult;
import com.example.backend.websocket.RunRequestResultIdDto;
import com.example.backend.websocket.WebSocketEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@Slf4j
public class JupyterService {

    private final JupyterClient jupyterClient;

    private final CodeGeneratingService codeGeneratingService;

    public JupyterService(JupyterClient jupyterClient, CodeGeneratingService codeGeneratingService) {
        this.jupyterClient = jupyterClient;
        this.codeGeneratingService = codeGeneratingService;
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

    public ResultDto readFinalResult(CodeValueToCompileDto data) throws ExecutionException, InterruptedException {
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
            Future<RunRequestResultIdDto> futureTask = threadpool.submit(() -> sendRunRequest(codeToCompile));

            while (!futureTask.isDone()) {

            }
            RunRequestResultIdDto runRequestResultId = futureTask.get();

            threadpool.shutdown();

            RunRequestResult result = readRunRequestResult(runRequestResultId.getRequestId());
            while (result.getRequestStatus() == RunRequestResult.RequestStatusLevel.PENDING) {
                result = readRunRequestResult(runRequestResultId.getRequestId());
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
        return ResultDto.builder().finalResult(((double) successes / valuesToCheck.length) * 100).build();
    }

}
