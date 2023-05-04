package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.addUser(userDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeUser(@PathVariable Long id) {
        userService.removeUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{userId}/role/{roleId}")
    public ResponseEntity<UserDto> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        return ResponseEntity.ok(userService.assignRoleToUser(userId, roleId));
    }

    @PutMapping("/{userId}/resultsQuestion/{questionId}")
    public ResponseEntity<UserResultsDto> addAnswerAndQuestionScoreToStudent(@PathVariable Long userId, @PathVariable Long questionId, @RequestBody ScoreAnswerDto scoreAnswerDto) {
        var userResultsDto = userService.addQuestionAnswerAndQuestionScoreToStudent(userId, questionId, scoreAnswerDto);
        return ResponseEntity.ok(userResultsDto);
    }

    @PutMapping("/{userId}/resultsProblem/{problemId}")
    public ResponseEntity<UserProblemResultDto> addAnswerAndProblemPercentageToStudent(@PathVariable Long userId, @PathVariable Long problemId, @RequestBody ScoreAnswerDto scoreAnswerDto) {
        var userProblemResultsDto = userService.addAnswerAndProblemPercentageToStudent(userId, problemId, scoreAnswerDto);
        return ResponseEntity.ok(userProblemResultsDto);
    }

    @GetMapping("/{userId}/resultsQuestions")
    public ResponseEntity<List<UserResultsDto>> getResultsScores(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getQuestionsResultsForStudent(userId));
    }

    @GetMapping("/{userId}/resultsProblems")
    public ResponseEntity<List<UserProblemResultDto>> getProblemResultsScores(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getProblemsResultsForStudent(userId));
    }

    @GetMapping("/resultsQuiz/{userId}")
    public ResponseEntity<UserTestResultDto> getUserFinalScore(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.computeTestScoreForUser(userId));
    }

    @GetMapping("/resultsQuiz")
    public ResponseEntity<List<UserAndFinalScoreDto>> getAllUsersFinalScore() {
        return ResponseEntity.ok(userService.getAllUsersFinalScores());
    }
}
