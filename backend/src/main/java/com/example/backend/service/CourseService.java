package com.example.backend.service;

import com.example.backend.dto.CourseDto;
import com.example.backend.dto.LessonDto;
import com.example.backend.entity.Course;
import com.example.backend.entity.CourseType;
import com.example.backend.entity.Lesson;
import com.example.backend.entity.enums.Difficulty;
import com.example.backend.exceptions.CrudOperationException;
import com.example.backend.repository.CourseRepository;
import com.example.backend.repository.CourseTypeRepository;
import com.example.backend.repository.LessonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    private final CourseTypeRepository courseTypeRepository;

    private final LessonRepository lessonRepository;

    public CourseService(CourseRepository courseRepository, CourseTypeRepository courseTypeRepository, LessonRepository lessonRepository) {
        this.courseRepository = courseRepository;
        this.courseTypeRepository = courseTypeRepository;
        this.lessonRepository = lessonRepository;
    }

    public CourseDto addCourse(CourseDto courseDto) {
        Course course = Course.builder()
                .name(courseDto.getName())
                .description(courseDto.getDescription())
                .difficulty(Difficulty.valueOf(courseDto.getDifficulty()))
                .build();

        if (course.getCourseTypes() == null || course.getCourseTypes().isEmpty()) {
            course.setCourseTypes(new HashSet<>());
        }

        CourseType courseType = CourseType.builder().type(courseDto.getCourseType()).build();

        if (courseTypeRepository.findByType(courseDto.getCourseType()).isEmpty()) {
            courseTypeRepository.save(courseType);
        }

        Optional<CourseType> optional = courseTypeRepository.findByType(courseType.getType());
        if (optional.isEmpty()) {
            CourseType newCourseType = courseTypeRepository.save(courseType);
            course.getCourseTypes().add(newCourseType);
        } else {
            course.getCourseTypes().add(optional.get());
        }

        courseRepository.save(course);
        courseDto.setId(course.getCourseId());

        return courseDto;
    }


    public void removeCourse(Long id) {
        courseRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Course does not exist!");
        });
        courseRepository.deleteById(id);
    }

    @Transactional
    public CourseDto updateCourse(Long id, CourseDto courseDto) {
        Course course = courseRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Course does not exist!");
        });

        course.setName(courseDto.getName());
        course.setDescription(courseDto.getDescription());
        course.setDifficulty(Difficulty.valueOf(courseDto.getDifficulty()));

        CourseType courseType = CourseType.builder().type(courseDto.getCourseType()).build();

        course.setCourseTypes(new HashSet<>());

        Optional<CourseType> optional = courseTypeRepository.findByType(courseType.getType());
        if (optional.isEmpty()) {
            CourseType newCourseType = courseTypeRepository.save(courseType);
            course.getCourseTypes().add(newCourseType);
        } else {
            course.getCourseTypes().add(optional.get());
        }

        courseRepository.save(course);

        courseDto.setId(course.getCourseId());
        return courseDto;
    }

    public CourseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Course does not exist!");
        });

        return CourseDto.builder()
                .id(course.getCourseId())
                .name(course.getName())
                .description(course.getDescription())
                .difficulty(course.getDifficulty().toString())
                .courseType(course.getCourseTypes().iterator().next().getType())
                .build();
    }

    public List<CourseDto> getAllCourses() {
        Iterable<Course> courses = courseRepository.findAll();
        List<CourseDto> courseDtos = new ArrayList<>();

        courses.forEach(course ->
                courseDtos.add(CourseDto.builder()
                        .id(course.getCourseId())
                        .name(course.getName())
                        .description(course.getDescription())
                        .difficulty(course.getDifficulty().toString())
                        .courseType(course.getCourseTypes().iterator().next().getType())
                        .build()));
        return courseDtos;
    }

    @Transactional
    public LessonDto addLessonToCourse(Long courseId, long lessonId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> {
            throw new CrudOperationException("Course does not exist!");
        });

        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> {
            throw new CrudOperationException("Lesson does not exist!");
        });

        if (course.getLessons() == null || course.getLessons().isEmpty()) {
            course.setLessons(new ArrayList<>());
        }

        int noLesson = course.getLessons().size() + 1;

        lesson.setCourse(course);
        lesson.setNoLesson(noLesson);
        course.getLessons().add(lesson);
        courseRepository.save(course);


        return LessonDto.builder()
                .id(lessonId)
                .name(lesson.getName())
                .description(lesson.getDescription())
                .longDescription(lesson.getLongDescription())
                .expectedTime(lesson.getExpectedTime())
                .noLesson(lesson.getNoLesson())
                .build();
    }

    @Transactional
    public void removeLessonFromCourse(Long courseId, long lessonId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> {
            throw new CrudOperationException("Course does not exist!");
        });

        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> {
            throw new CrudOperationException("Lesson does not exist!");
        });

        course.getLessons().remove(lesson);
        lesson.setCourse(null);
        lessonRepository.save(lesson);

        courseRepository.save(course);
    }

    @Transactional
    public List<LessonDto> getAllCourseLessons(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> {
            throw new CrudOperationException("Course does not exist!");
        });

        List<Lesson> lessons = course.getLessons();

        return lessons.stream().map(lesson ->
                LessonDto.builder()
                        .expectedTime(lesson.getExpectedTime())
                        .id(lesson.getLessonId())
                        .name(lesson.getName())
                        .description(lesson.getDescription())
                        .longDescription(lesson.getLongDescription())
                        .noLesson(lesson.getNoLesson())
                        .build()
        ).toList();
    }

    public List<CourseDto> findByDifficultyIn(List<String> difficulties) {
        var diff = difficulties.stream().map(Difficulty::valueOf).toList();
        List<Course> courses = courseRepository.findByDifficultyIn(diff);

        return courses.stream().map(course -> CourseDto.builder()
                .id(course.getCourseId())
                .name(course.getName())
                .description(course.getDescription())
                .difficulty(course.getDifficulty().toString())
                .courseType(course.getCourseTypes().iterator().next().getType())
                .build()).toList();

    }

//    public List<CourseDto> getAllCoursesByCourseType(String courseType) {
//        Set<CourseType> courseTypes = new HashSet<>();
//        courseTypes.add(CourseType.builder().type(courseType).build());
//        List<Course> courses = courseRepository.getAllByCourseTypesContains(courseTypes);
//
//        List<CourseDto> courseDtos = new ArrayList<>();
//
//        courses.forEach(course ->
//                courseDtos.add(CourseDto.builder()
//                        .id(course.getCourseId())
//                        .name(course.getName())
//                        .description(course.getDescription())
//                        .difficulty(course.getDifficulty().toString())
//                        .courseType(course.getCourseTypes().iterator().next().getType())
//                        .build()));
//        return courseDtos;
//    }
}
