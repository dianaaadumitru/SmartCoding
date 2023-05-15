package com.example.backend.controller;

import com.example.backend.dto.CourseTypeDto;
import com.example.backend.service.CourseTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courseTypes")
public class CourseTypeController {
    private final CourseTypeService courseTypeService;

    public CourseTypeController(CourseTypeService courseTypeService) {
        this.courseTypeService = courseTypeService;
    }

    @GetMapping
    public ResponseEntity<List<CourseTypeDto>> getAllCourseTypes() {
        return ResponseEntity.ok(courseTypeService.getAllCourseTypes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseTypeDto> getCourseTypeById(@PathVariable Long id) {
        return ResponseEntity.ok(courseTypeService.getCourseTypeById(id));
    }

    @PostMapping
    public ResponseEntity<CourseTypeDto> addCourseType(@RequestBody CourseTypeDto courseTypeDto) {
        return ResponseEntity.ok(courseTypeService.addCourseType(courseTypeDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseTypeDto> updateCourseType(@PathVariable Long id, @RequestBody CourseTypeDto courseTypeDto) {
        return ResponseEntity.ok(courseTypeService.updateCourseType(id, courseTypeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCourseType(@PathVariable Long id) {
        courseTypeService.removeCourseType(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
