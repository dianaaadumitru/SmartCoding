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
import java.util.stream.Collectors;

import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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
    public void givenCourseAndCourseDto_updateCourse_updateCourseIfFound() {
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
    public void givenCourseAndCourseDto_updateCourse_throwExceptionIfCourseDoesntExist() {
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

    @Test
    public void givenCourseWithLessons_testGetAllCourseLessons_returnListOfLessonsIfFound() {
        //given
        Course course = Course.builder()
                .courseId(1L)
                .name("course name")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        Lesson lesson1 = Lesson.builder()
                .lessonId(2L)
                .name("lesson1")
                .build();

        Lesson lesson2 = Lesson.builder()
                .lessonId(3L)
                .name("lesson2")
                .build();

        List<Lesson> lessons = new ArrayList<>();
        lessons.add(lesson1);
        lessons.add(lesson2);

        course.setLessons(lessons);

        // Mock repository behavior
        when(courseRepository.findById(course.getCourseId())).thenReturn(Optional.of(course));

        // when
        List<LessonDto> lessonDtos = courseService.getAllCourseLessons(course.getCourseId());

        // assert
        assertEquals(2, lessonDtos.size());

        LessonDto lessonDto1 = lessonDtos.get(0);
        assertEquals(2L, lessonDto1.getId());
        assertEquals("lesson1", lessonDto1.getName());

        LessonDto lessonDto2 = lessonDtos.get(1);
        assertEquals(3L, lessonDto2.getId());
        assertEquals("lesson2", lessonDto2.getName());
    }

    @Test
    public void givenLessonAndCourse_removeLessonFromCourse_removeLessonFromCourse() {
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

        List<Lesson> lessons = new ArrayList<>();
        lessons.add(lesson);
        course.setLessons(lessons);

        when(courseRepository.findById(course.getCourseId())).thenReturn(Optional.of(course));
        when(lessonRepository.findById(lesson.getLessonId())).thenReturn(Optional.of(lesson));

        // Act
        courseService.removeLessonFromCourse(course.getCourseId(), lesson.getLessonId());

        // Assert
        assertTrue(course.getLessons().isEmpty());
        assertNull(lesson.getCourse());
        verify(lessonRepository, times(1)).save(lesson);
        verify(courseRepository, times(1)).save(course);

    }

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
        // given
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

    @Test
    void findByDifficultyIn_ValidDifficulties_ReturnsCourseDtos() {
        // given
        List<String> difficulties = Arrays.asList("BEGINNER", "INTERMEDIATE");

        List<Course> courses = new ArrayList<>();
        courses.add(Course.builder()
                .courseId(1L)
                .name("Course 1")
                .description("Description 1")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build());
        courses.add((Course.builder()
                .courseId(2L)
                .name("Course 2")
                .description("Description 2")
                .difficulty(Difficulty.INTERMEDIATE)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build()));

        when(courseRepository.findByDifficultyIn(difficulties.stream()
                .map(Difficulty::valueOf)
                .collect(Collectors.toList())))
                .thenReturn(courses);

        // when
        List<CourseDto> result = courseService.findByDifficultyIn(difficulties);

        // assert
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Course 1", result.get(0).getName());
        assertEquals("Description 1", result.get(0).getDescription());
        assertEquals("BEGINNER", result.get(0).getDifficulty());
        assertEquals("course type", result.get(0).getCourseType());
        assertEquals(2L, result.get(1).getId());
        assertEquals("Course 2", result.get(1).getName());
        assertEquals("Description 2", result.get(1).getDescription());
        assertEquals("INTERMEDIATE", result.get(1).getDifficulty());
        assertEquals("course type", result.get(1).getCourseType());
    }

    @Test
    void givenCourseWithLessonsAndLessonNo_getCourseLessonByNoLesson_returnLessonIfExists() {
        // given
        int lessonNo = 2;
        Course course = Course.builder()
                .courseId(1L)
                .name("course name")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        Lesson lesson1 = Lesson.builder()
                .lessonId(2L)
                .name("lesson1")
                .noLesson(1)
                .build();

        Lesson lesson2 = Lesson.builder()
                .lessonId(3L)
                .name("lesson2")
                .noLesson(2)
                .build();

        List<Lesson> lessons = new ArrayList<>();
        lessons.add(lesson1);
        lessons.add(lesson2);

        course.setLessons(lessons);

        when(courseRepository.findById(course.getCourseId())).thenReturn(Optional.of(course));

        // when
        LessonDto expected = courseService.getCourseLessonByNoLesson(course.getCourseId(), lessonNo);
        LessonDto actual = LessonDto.builder()
                .name(lesson2.getName())
                .id(lesson2.getLessonId())
                .noLesson(lesson2.getNoLesson())
                .build();

        // assert
        assertEquals(expected, actual);
    }

    @Test
    void findByDifficultyIn_EmptyDifficulties_ReturnsEmptyList() {
        // given
        List<String> difficulties = new ArrayList<>();

        // when
        List<CourseDto> result = courseService.findByDifficultyIn(difficulties);

        // assert
        assertTrue(result.isEmpty());
    }
}