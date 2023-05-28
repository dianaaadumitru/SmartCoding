package com.example.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CodeGeneratingServiceTest {
    @Test
    void generateCode_ReturnsExpectedCode() {
        // Arrange
        CodeGeneratingService codeGeneratingService = new CodeGeneratingService();
        String code = "def foo(): \n\treturn true";
        String value = "bar";
        String expectedCode = "def foo(): \n\treturn true\nfoo(bar)";

        // Act
        String generatedCode = codeGeneratingService.generateCode(code, value);

        // Assert
        assertEquals(expectedCode, generatedCode);
    }

}