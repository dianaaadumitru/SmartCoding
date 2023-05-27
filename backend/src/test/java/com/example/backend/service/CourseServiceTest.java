package com.example.backend.service;

import com.example.backend.dto.CourseDto;
import com.example.backend.dto.LessonDto;
import com.example.backend.entity.Course;
import com.example.backend.entity.CourseType;
import com.example.backend.entity.Lesson;
import com.example.backend.entity.enums.Difficulty;
import com.example.backend.repository.CourseRepository;
import com.example.backend.repository.CourseTypeRepository;
import com.example.backend.repository.LessonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import org.junit.jupiter.api.function.Executable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private CourseTypeRepository courseTypeRepository;

    @Test
    public void givenCourseDto_addCourse_returnEqualCourseDto() {
        // given
        CourseDto courseDto = CourseDto.builder()
                .name("course name")
                .difficulty("BEGINNER")
                .build();

        Course course = Course.builder()
                .name(courseDto.getName())
                .difficulty(Difficulty.valueOf(courseDto.getDifficulty()))
                .build();

        when(courseRepository.save(ArgumentMatchers.any(Course.class))).thenReturn(course);

        // when
        CourseDto newCourseDto = courseService.addCourse(courseDto);

        // then
        assertEquals(courseDto, newCourseDto);
        verify(courseRepository).save(ArgumentMatchers.any(Course.class));
    }

    @Test
    public void givenAllCourses_getCourses_returnAllCourses() {
        // given
        List<Course> courses = new ArrayList<>();
        courses.add(Course.builder()
                .name("course name")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build());

        when(courseRepository.findAll()).thenReturn(courses);

        // when
        List<CourseDto> expected = courseService.getAllCourses();
        List<CourseDto> actual = courses.stream().map(course ->
                CourseDto.builder()
                        .name(course.getName())
                        .difficulty(course.getDifficulty().toString())
                        .courseType(course.getCourseTypes().iterator().next().getType())
                        .build()).toList();

        // then
        assertEquals(expected, actual);
        verify(courseRepository).findAll();
    }

    @Test
    public void givenCourse_removeCourse_deleteCourseIfFound() {
        // given
        Course course = Course.builder()
                .name("course name")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        when(courseRepository.findById(course.getCourseId())).thenReturn(Optional.of(course));

        // when
        courseService.removeCourse(course.getCourseId());

        // then
        verify(courseRepository).deleteById(course.getCourseId());
    }

    @Test
    public void givenCourse_removeCourse_throwExceptionIfCourseDoesntExist() {
        // given
        Course course = Course.builder()
                .name("course name")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> courseService.removeCourse(course.getCourseId());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenCourseAndQuestionDto_updateCourse_updateCourseIfFound() {
        // given
        Course course = Course.builder()
                .name("course name")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        CourseDto newCourseDto = CourseDto.builder()
                .name("course update")
                .difficulty("BEGINNER")
                .courseType("course type")
                .build();

        when(courseRepository.findById(course.getCourseId())).thenReturn(Optional.of(course));

        // when
        courseService.updateCourse(course.getCourseId(), newCourseDto);

        // then
        assertEquals(course.getName(), newCourseDto.getName());
        verify(courseRepository).save(ArgumentMatchers.any(Course.class));
        verify(courseRepository).findById(course.getCourseId());
    }

    @Test
    public void givenCourseAndQuestionDto_updateCourse_throwExceptionIfCourseDoesntExist() {
        // given
        Course course = Course.builder()
                .courseId(1L)
                .name("course name")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        CourseDto newCourseDto = CourseDto.builder()
                .id(course.getCourseId())
                .name("course update")
                .difficulty("BEGINNER")
                .courseType("course type")
                .build();

        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> courseService.updateCourse(course.getCourseId(), newCourseDto);

        // then
        assertThrows(RuntimeException.class, call);
    }


    @Test
    public void givenCourse_findCourseById_returnEqualCourseIfFound() {
        // given
        Course course = Course.builder()
                .courseId(1L)
                .name("course name")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        when(courseRepository.findById(course.getCourseId())).thenReturn(Optional.of(course));

        // when
        CourseDto expected = courseService.getCourseById(course.getCourseId());
        CourseDto actual = CourseDto.builder()
                .id(course.getCourseId())
                .name(course.getName())
                .difficulty(course.getDifficulty().toString())
                .courseType(course.getCourseTypes().iterator().next().getType())
                .build();

        // then
        assertEquals(expected, actual);
        verify(courseRepository).findById(course.getCourseId());
    }

    @Test
    public void givenCourse_findCourseById_throwExceptionIfCourseDoesntExist() {
        // given
        Course course = Course.builder()
                .courseId(1L)
                .name("course name")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> courseService.getCourseById(course.getCourseId());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenLessonAndCourse_addLessonToCourse_addLessonToCourse() {
        Course course = Course.builder()
                .courseId(1L)
                .name("course name")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        Lesson lesson = Lesson.builder()
                .lessonId(2L)
                .name("lesson")
                .build();

        when(courseRepository.findById(course.getCourseId())).thenReturn(Optional.of(course));
        when(lessonRepository.findById(lesson.getLessonId())).thenReturn(Optional.of(lesson));

        // when
        LessonDto expected = courseService.addLessonToCourse(course.getCourseId(), lesson.getLessonId());
        LessonDto actual = LessonDto.builder()
                .id(lesson.getLessonId())
                .name(lesson.getName())
                .noLesson(1)
                .build();

        // then
        assertEquals(expected, actual);
        verify(courseRepository).findById(course.getCourseId());
        verify(lessonRepository).findById(lesson.getLessonId());
    }

    @Test
    public void givenLessonAndNotOkCourse_addLessonToCourse_throwExceptionIfCourseDoesntExist() {
        Course course = Course.builder()
                .courseId(1L)
                .name("course name")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        Lesson lesson = Lesson.builder()
                .lessonId(2L)
                .name("lesson")
                .build();

        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> courseService.addLessonToCourse(course.getCourseId(), lesson.getLessonId());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenNotOkLessonAndCourse_addLessonToCourse_throwExceptionIfLessonDoesntExist() {
        Course course = Course.builder()
                .courseId(1L)
                .name("course name")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        Lesson lesson = Lesson.builder()
                .lessonId(2L)
                .name("lesson")
                .build();

        when(courseRepository.findById(course.getCourseId())).thenReturn(Optional.of(course));
        when(lessonRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> courseService.addLessonToCourse(course.getCourseId(), lesson.getLessonId());

        // then
        assertThrows(RuntimeException.class, call);
        verify(courseRepository).findById(course.getCourseId());
    }


//    @Test
//    public void givenLessonAndCourse_removeLessonFromCourse_removeLessonFromCourse() {
//        Course course = Course.builder()
//                .courseId(1L)
//                .name("course name")
//                .difficulty(Difficulty.BEGINNER)
//                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
//                .build();
//
//        Lesson lesson = Lesson.builder()
//                .lessonId(2L)
//                .name("lesson")
//                .build();
//
//        when(courseRepository.findById(course.getCourseId())).thenReturn(Optional.of(course));
//        when(lessonRepository.findById(lesson.getLessonId())).thenReturn(Optional.of(lesson));
//        when(courseRepository.save(ArgumentMatchers.any(Course.class))).thenReturn(course);
//        when(lessonRepository.save(ArgumentMatchers.any(Lesson.class))).thenReturn(lesson);
//
//
//        // when
//        courseService.removeLessonFromCourse(course.getCourseId(), lesson.getLessonId());
//
//        // then
//        verify(courseRepository).findById(course.getCourseId());
//        verify(lessonRepository).findById(lesson.getLessonId());
//        verify(courseRepository).save(ArgumentMatchers.any(Course.class));
//        verify(lessonRepository).save(ArgumentMatchers.any(Lesson.class));
//
//    }

    @Test
    public void givenLessonAndNotOkCourse_removeLessonFromCourse_throwExceptionIfCourseDoesntExist() {
        Course course = Course.builder()
                .courseId(1L)
                .name("course name")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        Lesson lesson = Lesson.builder()
                .lessonId(2L)
                .name("lesson")
                .build();

        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> courseService.removeLessonFromCourse(course.getCourseId(), lesson.getLessonId());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenNotOkLessonAndCourse_removeLessonFromCourse_throwExceptionIfLessonDoesntExist() {
        Course course = Course.builder()
                .courseId(1L)
                .name("course name")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        Lesson lesson = Lesson.builder()
                .lessonId(2L)
                .name("lesson")
                .build();

        when(courseRepository.findById(course.getCourseId())).thenReturn(Optional.of(course));
        when(lessonRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> courseService.removeLessonFromCourse(course.getCourseId(), lesson.getLessonId());

        // then
        assertThrows(RuntimeException.class, call);
        verify(courseRepository).findById(course.getCourseId());
    }

}