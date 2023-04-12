package com.example.backend.service;

import org.springframework.stereotype.Service;

@Service
public class CodeGeneratingService {

    private final UserService userService;

    public CodeGeneratingService(UserService userService) {
        this.userService = userService;
    }

    public String generateCode(Long userId) {
        return "";
    }

}
