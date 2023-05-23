package com.example.backend.controller;

import com.example.backend.dto.CourseDto;
import com.example.backend.dto.LessonDto;
import com.example.backend.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PostMapping
    public ResponseEntity<CourseDto> addCourse(@RequestBody CourseDto courseDto) {
        return ResponseEntity.ok(courseService.addCourse(courseDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long id, @RequestBody CourseDto courseDto) {
        return ResponseEntity.ok(courseService.updateCourse(id, courseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCourse(@PathVariable Long id) {
        courseService.removeCourse(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{courseId}/addLesson/{lessonId}")
    public ResponseEntity<LessonDto> addLessonToCourse(@PathVariable Long courseId, @PathVariable Long lessonId) {
        return ResponseEntity.ok(courseService.addLessonToCourse(courseId, lessonId));
    }

    @DeleteMapping("/{courseId}/removeLesson/{lessonId}")
    public ResponseEntity<Void> removeLessonFromCourse(@PathVariable Long courseId, @PathVariable Long lessonId) {
        courseService.removeLessonFromCourse(courseId, lessonId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{courseId}/lessons")
    public ResponseEntity<List<LessonDto>> getAllCourseLessons(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getAllCourseLessons(courseId));
    }

    @GetMapping("/{courseId}/lessons/noLesson/{noLesson}")
    public ResponseEntity<LessonDto> getCourseLessonByNoLesson(@PathVariable Long courseId, @PathVariable int noLesson) {
        return ResponseEntity.ok(courseService.getCourseLessonByNoLesson(courseId, noLesson));
    }

    @GetMapping("/difficulty")
    public ResponseEntity<List<CourseDto>> findByDifficultyIn(@RequestParam("difficulties") List<String> difficulties) {
        return ResponseEntity.ok(courseService.findByDifficultyIn(difficulties));
    }

//    @GetMapping("/courseTypes")
//    public ResponseEntity<List<CourseDto>> getAllCoursesByCourseType(@RequestParam String courseType) {
//        return ResponseEntity.ok(courseService.getAllCoursesByCourseType(courseType));
//    }
}
