package com.example.backend.controller;

import com.example.backend.entity.enums.Difficulty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/difficulties")
public class DifficultyController {
    @GetMapping
    public ResponseEntity<Difficulty[]> getDifficulties() {
        return ResponseEntity.ok(Difficulty.values());
    }
}
