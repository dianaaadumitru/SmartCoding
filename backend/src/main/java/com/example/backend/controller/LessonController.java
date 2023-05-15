package com.example.backend.controller;

import com.example.backend.dto.LessonDto;
import com.example.backend.dto.ProblemDto;
import com.example.backend.service.LessonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lessons")
public class LessonController {
    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping
    public ResponseEntity<List<LessonDto>> getAllCourses() {
        return ResponseEntity.ok(lessonService.getAllLessons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDto> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.getLessonById(id));
    }

    @PostMapping
    public ResponseEntity<LessonDto> addCourse(@RequestBody LessonDto lessonDto) {
        return ResponseEntity.ok(lessonService.addLesson(lessonDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonDto> updateCourse(@PathVariable Long id, @RequestBody LessonDto lessonDto) {
        return ResponseEntity.ok(lessonService.updateLesson(id, lessonDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCourse(@PathVariable Long id) {
        lessonService.removeLesson(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{lessonId}/addProblem/{problemId}")
    public ResponseEntity<ProblemDto> addProblemToLesson(@PathVariable Long lessonId, @PathVariable Long problemId) {
        return ResponseEntity.ok(lessonService.addProblemToLesson(lessonId, problemId));
    }

    @DeleteMapping("/{lessonId}/removeProblem/{problemId}")
    public ResponseEntity<Void> removeProblemFromLesson(@PathVariable Long lessonId, @PathVariable Long problemId) {
        lessonService.removeProblemFromLesson(lessonId, problemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{lessonId}/problems")
    public ResponseEntity<List<ProblemDto>> getAllLessonProblems(@PathVariable Long lessonId) {
        return ResponseEntity.ok(lessonService.getAllLessonProblems(lessonId));
    }
}
