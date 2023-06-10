package com.example.backend.service;

import com.example.backend.client.JupyterClient;
import com.example.backend.dto.CodeDto;
import com.example.backend.dto.CodeValueToCompileDto;
import com.example.backend.dto.ResultDto;
import com.example.backend.exceptions.CrudOperationException;
import com.example.backend.jupyter.model.JupyterSessionDto;
import com.example.backend.websocket.RunRequestResult;
import com.example.backend.websocket.RunRequestResultIdDto;
import com.example.backend.websocket.WebSocketEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
        if (data.getCode().isEmpty()) {
            throw new CrudOperationException("empty code");
        }

        List<String> valuesToCheck = Arrays.asList(data.getValuesToCheckCode().split(";"));
        List<String> resultsToCheck = Arrays.asList(data.getResultsToCheckCode().split(";"));
        String pythonCodeStatus = "";

        int successes = 0;
        StringBuilder printedResultBuilder = new StringBuilder();

        for (int i = 0; i < valuesToCheck.size(); i++) {
            String currentValue = valuesToCheck.get(i).strip();
            if ("String".equals(data.getValuesType())) {
                currentValue = "\"" + currentValue + "\"";
            }

            String codeToCompile = ("RETURN".equals(data.getReturnType()))
                    ? codeGeneratingService.generateCode(data.getCode(), currentValue)
                    : data.getCode();

            RunRequestResult result = sendAndAwaitRunRequest(codeToCompile);
            pythonCodeStatus = result.getCodeExecutionResult().getPythonCodeStatus();
            if (resultsToCheck.get(i).strip().equals(result.getCodeExecutionResult().getReturnedResult())) {
                successes++;
            }

            String printedResult = result.getCodeExecutionResult().getPrintedResult();
            if (printedResult != null) {
                printedResultBuilder.append(printedResult);
            }
        }

        double finalResult = ((double) successes / valuesToCheck.size()) * 100;

        ResultDto resultDto = ("RETURN".equals(data.getReturnType()))
                ? ResultDto.builder().printedResult(null).finalResult(finalResult).pythonCodeStatus(pythonCodeStatus).build()
                : ResultDto.builder().printedResult(printedResultBuilder.toString()).finalResult(null).pythonCodeStatus(pythonCodeStatus).build();

        return resultDto;
    }

    private RunRequestResult sendAndAwaitRunRequest(String codeToCompile) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            Future<RunRequestResultIdDto> futureTask = executorService.submit(() -> sendRunRequest(codeToCompile));
            RunRequestResultIdDto runRequestResultId = futureTask.get();

            RunRequestResult result = readRunRequestResult(runRequestResultId.getRequestId());
            while (result.getRequestStatus() == RunRequestResult.RequestStatusLevel.PENDING) {
                Thread.sleep(1000);
                result = readRunRequestResult(runRequestResultId.getRequestId());
            }

            return result;
        } finally {
            executorService.shutdown();
        }
    }


    public RunRequestResult readFinalResultDeveloper(CodeDto data) throws ExecutionException, InterruptedException {
        if (data.getCode().isEmpty()) {
            throw new CrudOperationException("empty code");
        }

        ExecutorService threadpool = Executors.newCachedThreadPool();
        Future<RunRequestResultIdDto> futureTask = threadpool.submit(() -> sendRunRequest(data.getCode()));

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
        }
        return result;
    }

}
