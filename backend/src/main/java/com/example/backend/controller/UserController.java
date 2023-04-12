package com.example.backend.controller;

import com.example.backend.dto.UserAndFinalScoreDto;
import com.example.backend.dto.UserDto;
import com.example.backend.dto.UserResultsDto;
import com.example.backend.dto.UserTestResultDto;
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
    public ResponseEntity<UserDto> assignTypeToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        return ResponseEntity.ok(userService.assignRoleToUser(userId, roleId));
    }

    @PutMapping("/{userId}/results/{questionId}")
    public ResponseEntity<UserResultsDto> addScoreToUser(@PathVariable Long userId, @PathVariable Long questionId, @RequestParam double score, @RequestParam String answer) {
        var userResultsDto = userService.addAnswerAndQuestionScoreToStudent(userId, questionId, score, answer);
        return ResponseEntity.ok(userResultsDto);
    }

    @GetMapping("/{userId}/results")
    public ResponseEntity<List<UserResultsDto>> getResultsScores(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getResultsForStudent(userId));
    }

    @GetMapping("/finalResults/{userId}")
    public ResponseEntity<UserTestResultDto> getUserFinalScore(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.computeTestScoreForUser(userId));
    }

    @GetMapping("/finalResults")
    public ResponseEntity<List<UserAndFinalScoreDto>> getAllUsersFinalScore() {
        return ResponseEntity.ok(userService.getAllUsersFinalScores());
    }
}
