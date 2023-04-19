package com.example.backend.utils;

public class ExtractMethodNameFromCode {
    public static String extractMethodName(String code) {
        String[] codeWords = code.split(" ");
        return codeWords[1].substring(0, codeWords[1].indexOf("("));
    }
}
