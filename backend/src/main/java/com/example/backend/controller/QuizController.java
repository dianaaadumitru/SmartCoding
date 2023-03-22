package com.example.backend.controller;

import com.example.backend.dto.QuizDto;
import com.example.backend.service.QuizService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quizzes")
public class QuizController {
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public ResponseEntity<List<QuizDto>> getAllQuizs() {
        return ResponseEntity.ok(quizService.getAllQuizzes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDto> getQuizById(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.getQuizById(id));
    }

    @PostMapping
    public ResponseEntity<QuizDto> addQuiz(@RequestBody QuizDto quizDto) {
        return ResponseEntity.ok(quizService.addQuiz(quizDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizDto> updateQuiz(@PathVariable Long id, @RequestBody QuizDto quizDto) {
        return ResponseEntity.ok(quizService.updateQuiz(id, quizDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeQuiz(@PathVariable Long id) {
        quizService.removeQuiz(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
