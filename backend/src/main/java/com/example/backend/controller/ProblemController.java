package com.example.backend.controller;

import com.example.backend.dto.ProblemDto;
import com.example.backend.dto.UserSolutionForProblemDto;
import com.example.backend.service.ProblemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/problems")
public class ProblemController {
    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }


    @GetMapping
    public ResponseEntity<List<ProblemDto>> getAllProblrms() {
        return ResponseEntity.ok(problemService.getAllProblems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProblemDto> getProblemById(@PathVariable Long id) {
        return ResponseEntity.ok(problemService.getProblemById(id));
    }

    @PostMapping
    public ResponseEntity<ProblemDto> addProblem(@RequestBody ProblemDto problemDto) {
        return ResponseEntity.ok(problemService.addProblem(problemDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProblemDto> updateProblem(@PathVariable Long id, @RequestBody ProblemDto problemDto) {
        return ResponseEntity.ok(problemService.updateProblem(id, problemDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeProblem(@PathVariable Long id) {
        problemService.removeProblem(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/userScores")
    public ResponseEntity<List<UserSolutionForProblemDto>> getAllUsersSolutionsForProblem(@PathVariable Long id) {
        return ResponseEntity.ok(problemService.getAllUsersSolutionsForProblem(id));
    }

    @GetMapping("/difficulty")
    public ResponseEntity<List<ProblemDto>> findByDifficultyIn(@RequestParam("difficulties") List<String> difficulties) {
        return ResponseEntity.ok(problemService.findByDifficultyIn(difficulties));
    }
}
