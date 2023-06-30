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

    @PutMapping("/{id}/changePassword")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @RequestBody ChangePasswordDto changePasswordDto) {
        userService.changePassword(id, changePasswordDto);
        return new ResponseEntity<>(HttpStatus.OK);
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

    @GetMapping("/{userId}/problems")
    public ResponseEntity<List<ProblemDto>> getAllProblemsSolvedByAUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getAllProblemsSolvedByAUser(userId));
    }

    @GetMapping("/{userId}/resultsQuestions")
    public ResponseEntity<List<UserResultsDto>> getResultsScores(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getQuestionsResultsForStudent(userId));
    }

    @GetMapping("/{userId}/resultsProblems")
    public ResponseEntity<List<UserProblemResultDto>> getProblemResultsScores(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getProblemsResultsForStudent(userId));
    }

    @GetMapping("/{userId}/resultsProblem/{problemId}")
    public ResponseEntity<ScoreAnswerDto> getProblemScoreForAProblemSoledByUser(@PathVariable Long userId, @PathVariable Long problemId) {
        return ResponseEntity.ok(userService.getProblemScoreForAProblemSoledByUser(userId, problemId));
    }

    @GetMapping("/topCourses")
    public ResponseEntity<List<CourseDto>> findTop8Courses() {
        return ResponseEntity.ok(userService.findTop8Courses());
    }

    @GetMapping("/topProblems")
    public ResponseEntity<List<ProblemDto>> findTop8Problems() {
        return ResponseEntity.ok(userService.findTop8Problems());
    }

    @PutMapping("/{userId}/courses/{courseId}")
    public ResponseEntity<CourseDto> addCourseToStudent(@PathVariable Long userId, @PathVariable Long courseId) {
        return ResponseEntity.ok(userService.addCourseToStudent(userId, courseId));
    }

    @DeleteMapping("/{userId}/courses/{courseId}")
    public ResponseEntity<Void> removeCourseFromStudent(@PathVariable Long userId, @PathVariable Long courseId) {
        userService.removeCourseFromStudent(userId, courseId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{userId}/courses")
    public ResponseEntity<List<CourseDto>> getUserCourses(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserCourses(userId));
    }

    @GetMapping("/{userId}/courses/{courseId}/isEnrolled")
    public ResponseEntity<Boolean> checkIfUserEnrolledToCourse(@PathVariable Long userId, @PathVariable Long courseId) {
        return ResponseEntity.ok(userService.checkIfUserEnrolledToCourse(userId, courseId));
    }

    @GetMapping("/{userId}/lessons/completedLessons/{lessonId}")
    public ResponseEntity<Boolean> checkIfAUserLessonIsComplete(@PathVariable Long userId, @PathVariable Long lessonId, @RequestParam Long courseId) {
        return ResponseEntity.ok(userService.checkIfAUserLessonIsComplete(userId, lessonId, courseId));
    }

    @PostMapping("/{userId}/lessons/completeLesson/{lessonId}")
    public ResponseEntity<Void> markLessonAsCompleted(@PathVariable Long userId, @PathVariable Long lessonId, @RequestParam Long courseId) {
        userService.markLessonAsCompleted(userId, lessonId, courseId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{userId}/courses/completedCourse/{courseId}")
    public ResponseEntity<Boolean> checkIfAllLessonsOfACourseAreComplete(@PathVariable Long userId, @PathVariable Long courseId, @RequestParam int length) {
        return ResponseEntity.ok(userService.checkIfAllLessonsOfACourseAreComplete(userId, courseId, length));
    }

    @PostMapping("/{userId}/lessons/addLesson/{lessonId}")
    public ResponseEntity<LessonDto> addEnrolledLessonToUser(@PathVariable Long userId, @PathVariable Long lessonId, @RequestParam Long courseId) {
        return ResponseEntity.ok(userService.addEnrolledLessonToUser(userId, lessonId, courseId));
    }
}
