package com.example.backend.service;

import com.example.backend.dto.LessonDto;
import com.example.backend.dto.ProblemDto;
import com.example.backend.entity.Lesson;
import com.example.backend.entity.Problem;
import com.example.backend.entity.enums.Difficulty;
import com.example.backend.entity.enums.ReturnType;
import com.example.backend.repository.LessonRepository;
import com.example.backend.repository.ProblemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {
    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private LessonService lessonService;

    @Mock
    private ProblemRepository problemRepository;

    @Test
    public void givenLessonDto_addLesson_returnEqualLessonDto() {
        // given
        LessonDto lessonDto = LessonDto.builder()
                .name("test")
                .build();

        Lesson lesson = Lesson.builder()
                .name(lessonDto.getName())
                .build();

        when(lessonRepository.save(ArgumentMatchers.any(Lesson.class))).thenReturn(lesson);

        // when
        LessonDto created = lessonService.addLesson(lessonDto);

        // then
        assertEquals(created, lessonDto);
        verify(lessonRepository).save(ArgumentMatchers.any(Lesson.class));
    }

    @Test
    public void givenAllLessons_getLessons_returnAllLessons() {
        // given
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(Lesson.builder().name("test").build());

        when(lessonRepository.findAll()).thenReturn(lessons);

        // when
        List<LessonDto> expected = lessonService.getAllLessons();
        List<LessonDto> lessonDtos = lessons.stream().map(lesson ->
                LessonDto.builder()
                        .name(lesson.getName())
                        .build()).toList();

        // then
        assertEquals(expected, lessonDtos);
        verify(lessonRepository).findAll();
    }

    @Test
    public void givenLesson_removeLesson_deleteLessonIfFound() {
        // given
        Lesson lesson = Lesson.builder()
                .lessonId(1L)
                .name("test")
                .build();

        when(lessonRepository.findById(lesson.getLessonId())).thenReturn(Optional.of(lesson));

        // when
        lessonService.removeLesson(lesson.getLessonId());

        // then
        verify(lessonRepository).deleteById(lesson.getLessonId());
    }

    @Test
    public void givenLesson_removeLesson_throwExceptionIfLessonDoesntExist() {
        // given
        Lesson lesson = Lesson.builder()
                .lessonId(1L)
                .name("test")
                .build();

        when(lessonRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> lessonService.removeLesson(lesson.getLessonId());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenLessonAndLessonDto_updateLesson_updateLessonIfFound() {
        // given
        Lesson lesson = Lesson.builder()
                .lessonId(10L)
                .name("test")
                .build();

        LessonDto newLessonDto = LessonDto.builder()
                .name("new name").build();

        when(lessonRepository.findById(lesson.getLessonId())).thenReturn(Optional.of(lesson));

        // when
        lessonService.updateLesson(lesson.getLessonId(), newLessonDto);

        // then
        assertEquals(newLessonDto.getName(), lesson.getName());
        verify(lessonRepository).save(ArgumentMatchers.any(Lesson.class));
        verify(lessonRepository).findById(lesson.getLessonId());
    }

    @Test
    public void givenLessonAndLessonDto_updateLesson_throwExceptionIfLessonDoesntExist() {
        // given
        Lesson lesson = Lesson.builder()
                .lessonId(10L)
                .name("test")
                .build();

        LessonDto newLessonDto = LessonDto.builder()
                .name("new name").build();

        when(lessonRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> lessonService.updateLesson(lesson.getLessonId(), newLessonDto);

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenLesson_findLessonById_returnEqualLessonIfFound() {
        // given
        Lesson lesson = Lesson.builder()
                .lessonId(10L)
                .name("test")
                .build();

        when(lessonRepository.findById(lesson.getLessonId())).thenReturn(Optional.of(lesson));

        // when
        LessonDto expectedDto = lessonService.getLessonById(lesson.getLessonId());
        LessonDto actualDto = LessonDto.builder()
                .name(lesson.getName())
                .id(lesson.getLessonId())
                .build();

        // then
        assertEquals(actualDto, expectedDto);
        verify(lessonRepository).findById(lesson.getLessonId());
    }

    @Test
    public void givenLesson_findLessonById_throwExceptionIfLessonDoesntExist() {
        // given
        Lesson lesson = Lesson.builder()
                .lessonId(10L)
                .name("test")
                .build();

        when(lessonRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> lessonService.getLessonById(lesson.getLessonId());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenLessonAndProblem_addProblemToLesson_addProblemToLesson() {
        // given
        Lesson lesson = Lesson.builder()
                .lessonId(1L)
                .name("lesson")
                .build();

        Problem problem = Problem.builder()
                .problemId(2L)
                .name("problem name")
                .difficulty(Difficulty.BEGINNER)
                .returnType(ReturnType.RETURN)
                .build();

        when(problemRepository.findById(problem.getProblemId())).thenReturn(Optional.of(problem));
        when(lessonRepository.findById(lesson.getLessonId())).thenReturn(Optional.of(lesson));

        // when
        ProblemDto expected = lessonService.addProblemToLesson(lesson.getLessonId(), problem.getProblemId());
        ProblemDto actual = ProblemDto.builder()
                .problemId(problem.getProblemId())
                .name(problem.getName())
                .difficulty("BEGINNER")
                .returnType("RETURN")
                .build();

        // then
        assertEquals(expected, actual);
        verify(problemRepository).findById(problem.getProblemId());
        verify(lessonRepository).findById(lesson.getLessonId());
    }

    @Test
    public void givenProblemAndNotOkLesson_addProblemToLesson_throwExceptionIfLessonDoesntExist() {
        // given
        Lesson lesson = Lesson.builder()
                .lessonId(1L)
                .name("lesson")
                .build();

        Problem problem = Problem.builder()
                .problemId(2L)
                .name("problem name")
                .difficulty(Difficulty.BEGINNER)
                .returnType(ReturnType.RETURN)
                .build();

        when(lessonRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> lessonService.addProblemToLesson(lesson.getLessonId(), problem.getProblemId());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenNotOkProblemAndLesson_addProblemToLesson_throwExceptionIfProblemDoesntExist() {
        // given
        Lesson lesson = Lesson.builder()
                .lessonId(1L)
                .name("lesson")
                .build();

        Problem problem = Problem.builder()
                .problemId(2L)
                .name("problem name")
                .difficulty(Difficulty.BEGINNER)
                .returnType(ReturnType.RETURN)
                .build();

        when(lessonRepository.findById(lesson.getLessonId())).thenReturn(Optional.of(lesson));
        when(problemRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> lessonService.addProblemToLesson(lesson.getLessonId(), problem.getProblemId());

        // then
        assertThrows(RuntimeException.class, call);
        verify(lessonRepository).findById(lesson.getLessonId());
    }

    @Test
    public void givenLessonWithProblems_testGetAllLessonProblems_returnListOfProblemsIfFound() {
        Lesson lesson = Lesson.builder()
                .lessonId(1L)
                .name("lesson")
                .build();

        Problem problem1 = Problem.builder()
                .problemId(2L)
                .name("problem1 name")
                .difficulty(Difficulty.BEGINNER)
                .returnType(ReturnType.RETURN)
                .build();

        List<Problem> problems = new ArrayList<>();
        problems.add(problem1);

        lesson.setProblems(problems);

        // Mock repository behavior
        when(lessonRepository.findById(lesson.getLessonId())).thenReturn(Optional.of(lesson));

        // when
        List<ProblemDto> problemDtos = lessonService.getAllLessonProblems(lesson.getLessonId());

        // assert
        assertEquals(1, problemDtos.size());
    }

    @Test
    public void givenNotOkLessonWithProblems_testGetAllLessonProblems_returnListOfProblemsIfFound() {
        Lesson lesson = Lesson.builder()
                .lessonId(1L)
                .name("lesson")
                .build();

        Problem problem1 = Problem.builder()
                .problemId(2L)
                .name("problem1 name")
                .difficulty(Difficulty.BEGINNER)
                .returnType(ReturnType.RETURN)
                .build();

        List<Problem> problems = new ArrayList<>();
        problems.add(problem1);

        lesson.setProblems(problems);

        // Mock repository behavior
        when(lessonRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> lessonService.getAllLessonProblems(lesson.getLessonId());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenLessonAndProblem_removeProblemFromLesson_removeProblemFromLesson() {
        Lesson lesson = Lesson.builder()
                .lessonId(1L)
                .name("lesson")
                .build();

        Problem problem = Problem.builder()
                .problemId(2L)
                .name("problem1 name")
                .difficulty(Difficulty.BEGINNER)
                .returnType(ReturnType.RETURN)
                .build();

        List<Problem> problems = new ArrayList<>();
        problems.add(problem);
        lesson.setProblems(problems);

        when(problemRepository.findById(problem.getProblemId())).thenReturn(Optional.of(problem));
        when(lessonRepository.findById(lesson.getLessonId())).thenReturn(Optional.of(lesson));

        // Act
        lessonService.removeProblemFromLesson(lesson.getLessonId(), problem.getProblemId());

        // Assert
        assertTrue(lesson.getProblems().isEmpty());
        assertNull(lesson.getCourse());
        verify(lessonRepository, times(1)).save(lesson);
        verify(problemRepository, times(1)).save(problem);

    }

    @Test
    public void givenProblemAndNotOkLesson_removeProblemFromLesson_throwExceptionIfLessonDoesntExist() {
        Lesson lesson = Lesson.builder()
                .lessonId(1L)
                .name("lesson")
                .build();

        Problem problem = Problem.builder()
                .problemId(2L)
                .name("problem1 name")
                .difficulty(Difficulty.BEGINNER)
                .returnType(ReturnType.RETURN)
                .build();

        when(lessonRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> lessonService.removeProblemFromLesson(lesson.getLessonId(), problem.getProblemId());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenNotOkProblemAndLesson_removeProblemFromLesson_throwExceptionIfProblemDoesntExist() {
        // given
        Lesson lesson = Lesson.builder()
                .lessonId(1L)
                .name("lesson")
                .build();

        Problem problem = Problem.builder()
                .problemId(2L)
                .name("problem1 name")
                .difficulty(Difficulty.BEGINNER)
                .returnType(ReturnType.RETURN)
                .build();

        when(lessonRepository.findById(lesson.getLessonId())).thenReturn(Optional.of(lesson));
        when(problemRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> lessonService.removeProblemFromLesson(lesson.getLessonId(), problem.getProblemId());

        // then
        assertThrows(RuntimeException.class, call);
        verify(lessonRepository).findById(lesson.getLessonId());
    }


}