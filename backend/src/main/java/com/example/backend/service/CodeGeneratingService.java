package com.example.backend.service;

import com.example.backend.utils.ExtractMethodNameFromCode;
import org.springframework.stereotype.Service;

@Service
public class CodeGeneratingService {

    private final UserService userService;

    public CodeGeneratingService(UserService userService) {
        this.userService = userService;
    }

    public String generateCode(String code, String value) {
        StringBuilder sb = new StringBuilder();
        sb.append(code).append("\n");
        String method = ExtractMethodNameFromCode.extractMethodName(code);
        sb.append(method).append("(").append(value).append(")");
        return sb.toString();
    }

}
