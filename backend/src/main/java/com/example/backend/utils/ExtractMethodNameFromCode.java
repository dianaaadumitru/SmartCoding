package com.example.backend.utils;

public class ExtractMethodNameFromCode {
    public static String extractMethodName(String code) {
        String[] codeWords = code.split(" ");
        if (codeWords.length < 2 || !code.contains("(")) {
            return "";
        }
        return codeWords[1].substring(0, codeWords[1].indexOf("("));
    }
}
