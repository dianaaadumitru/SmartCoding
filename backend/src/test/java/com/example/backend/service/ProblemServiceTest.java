package com.example.backend.service;

import com.example.backend.dto.ProblemDto;
import com.example.backend.dto.UserSolutionForProblemDto;
import com.example.backend.entity.Problem;
import com.example.backend.entity.User;
import com.example.backend.entity.UserProblemResults;
import com.example.backend.entity.enums.Difficulty;
import com.example.backend.entity.enums.ReturnType;
import com.example.backend.repository.ProblemRepository;
import com.example.backend.repository.UserProblemResultsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProblemServiceTest {
    @Mock
    private ProblemRepository problemRepository;

    @InjectMocks
    private ProblemService problemService;

    @Mock
    private UserProblemResultsRepository userProblemResultsRepository;

    @Test
    public void givenProblemDto_addProblem_returnEqualProblemDto() {
        // given
        ProblemDto problemDto = ProblemDto.builder()
                .name("test")
                .difficulty("BEGINNER")
                .returnType("RETURN")
                .build();

        Problem problem = Problem.builder()
                .name(problemDto.getName())
                .returnType(ReturnType.RETURN)
                .difficulty(Difficulty.BEGINNER)
                .build();

        when(problemRepository.save(ArgumentMatchers.any(Problem.class))).thenReturn(problem);

        // when
        ProblemDto created = problemService.addProblem(problemDto);

        // then
        assertEquals(created, problemDto);
        verify(problemRepository).save(ArgumentMatchers.any(Problem.class));
    }

    @Test
    public void givenAllProblems_getProblems_returnAllProblems() {
        // given
        List<Problem> problems = new ArrayList<>();
        problems.add(Problem.builder()
                .name("test")
                .returnType(ReturnType.RETURN)
                .difficulty(Difficulty.BEGINNER)
                .build());

        when(problemRepository.findAll()).thenReturn(problems);

        // when
        List<ProblemDto> expected = problemService.getAllProblems();
        List<ProblemDto> quizDtos = problems.stream().map(problem ->
                ProblemDto.builder()
                        .name(problem.getName())
                        .returnType(problem.getReturnType().toString())
                        .difficulty(problem.getDifficulty().toString())
                        .build()).toList();

        // then
        assertEquals(expected, quizDtos);
        verify(problemRepository).findAll();
    }

    @Test
    public void givenProblem_removeProblem_deleteProblemIfFound() {
        // given
        Problem problem = Problem.builder()
                .problemId(1L)
                .name("test")
                .returnType(ReturnType.RETURN)
                .difficulty(Difficulty.BEGINNER)
                .build();

        when(problemRepository.findById(problem.getProblemId())).thenReturn(Optional.of(problem));

        // when
        problemService.removeProblem(problem.getProblemId());

        // then
        verify(problemRepository).deleteById(problem.getProblemId());
    }

    @Test
    public void givenProblem_removeProblem_throwExceptionIfProblemDoesntExist() {
        // given
        Problem problem = Problem.builder()
                .problemId(1L)
                .name("test")
                .returnType(ReturnType.RETURN)
                .difficulty(Difficulty.BEGINNER)
                .build();

        when(problemRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> problemService.removeProblem(problem.getProblemId());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenProblemAndProblemDto_updateProblem_updateProblemIfFound() {
        // given
        Problem problem = Problem.builder()
                .problemId(10L)
                .name("test")
                .returnType(ReturnType.RETURN)
                .difficulty(Difficulty.BEGINNER)
                .build();

        ProblemDto newProblemDto = ProblemDto.builder()
                .name("new name")
                .returnType(ReturnType.RETURN.toString())
                .difficulty(Difficulty.BEGINNER.toString())
                .build();

        when(problemRepository.findById(problem.getProblemId())).thenReturn(Optional.of(problem));

        // when
        problemService.updateProblem(problem.getProblemId(), newProblemDto);

        // then
        assertEquals(newProblemDto.getName(), problem.getName());
        verify(problemRepository).save(ArgumentMatchers.any(Problem.class));
        verify(problemRepository).findById(problem.getProblemId());
    }

    @Test
    public void givenProblemAndQuizDto_updateProblem_throwExceptionIfProblemDoesntExist() {
        // given
        Problem problem = Problem.builder()
                .problemId(10L)
                .name("test")
                .returnType(ReturnType.RETURN)
                .difficulty(Difficulty.BEGINNER)
                .build();

        ProblemDto newProblemDto = ProblemDto.builder()
                .name("new name")
                .returnType(ReturnType.RETURN.toString())
                .difficulty(Difficulty.BEGINNER.toString())
                .build();

        when(problemRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> problemService.updateProblem(problem.getProblemId(), newProblemDto);

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenProblem_findProblemById_returnEqualProblemIfFound() {
        // given
        Problem problem = Problem.builder()
                .problemId(10L)
                .name("test")
                .returnType(ReturnType.RETURN)
                .difficulty(Difficulty.BEGINNER)
                .build();

        when(problemRepository.findById(problem.getProblemId())).thenReturn(Optional.of(problem));

        // when
        ProblemDto expectedDto = problemService.getProblemById(problem.getProblemId());
        ProblemDto actualDto = ProblemDto.builder()
                .name(problem.getName())
                .difficulty(problem.getDifficulty().toString())
                .returnType(problem.getReturnType().toString())
                .problemId(problem.getProblemId())
                .build();

        // then
        assertEquals(actualDto, expectedDto);
        verify(problemRepository).findById(problem.getProblemId());
    }

    @Test
    public void givenProblem_findProblemById_throwExceptionIfProblemDoesntExist() {
        // given
        Problem problem = Problem.builder()
                .problemId(10L)
                .name("test")
                .returnType(ReturnType.RETURN)
                .difficulty(Difficulty.BEGINNER)
                .build();

        when(problemRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> problemService.getProblemById(problem.getProblemId());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenProblemAndUser_getAllUsersSolutionsForProblem_returnUsersSolutions() {
        // given
        Problem problem = Problem.builder()
                .problemId(10L)
                .name("test")
                .returnType(ReturnType.RETURN)
                .difficulty(Difficulty.BEGINNER)
                .build();

        User user1 = User.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        UserProblemResults result1 = UserProblemResults.builder()
                .user(user1)
                .percentage(80.0)
                .build();

        User user2 = User.builder()
                .firstName("Jane")
                .lastName("Smith")
                .build();

        UserProblemResults result2 = UserProblemResults.builder()
                .user(user2)
                .percentage(90.0)
                .build();

        List<UserProblemResults> userProblemResults = new ArrayList<>();
        userProblemResults.add(result1);
        userProblemResults.add(result2);

        when(userProblemResultsRepository.findByProblem_ProblemId(problem.getProblemId())).thenReturn(userProblemResults);

        // when
        List<UserSolutionForProblemDto> dtos = problemService.getAllUsersSolutionsForProblem(problem.getProblemId());

        // then
        assertEquals(2, dtos.size());

        UserSolutionForProblemDto dto1 = dtos.get(0);
        assertEquals("John", dto1.getUserFirsName());
        assertEquals("Doe", dto1.getUserLastName());
        assertEquals(80.0, dto1.getScore());

        UserSolutionForProblemDto dto2 = dtos.get(1);
        assertEquals("Jane", dto2.getUserFirsName());
        assertEquals("Smith", dto2.getUserLastName());
        assertEquals(90.0, dto2.getScore());
    }

    @Test
    void findByDifficultyIn_validDifficulties_ReturnsProblemDtos() {
        // given
        List<String> difficulties = Arrays.asList("BEGINNER", "INTERMEDIATE");

        List<Problem> problems = new ArrayList<>();
        problems.add(Problem.builder()
                .problemId(10L)
                .name("test1")
                .returnType(ReturnType.RETURN)
                .difficulty(Difficulty.BEGINNER)
                .build());
        problems.add(Problem.builder()
                .problemId(10L)
                .name("test2")
                .returnType(ReturnType.RETURN)
                .difficulty(Difficulty.INTERMEDIATE)
                .build());

        when(problemRepository.findByDifficultyIn(difficulties.stream()
                .map(Difficulty::valueOf)
                .collect(Collectors.toList())))
                .thenReturn(problems);

        // when
        List<ProblemDto> result = problemService.findByDifficultyIn(difficulties);

        // assert
        assertEquals(2, result.size());
        verify(problemRepository).findByDifficultyIn(difficulties.stream()
                .map(Difficulty::valueOf)
                .collect(Collectors.toList()));
    }

    @Test
    void findByDifficultyIn_EmptyDifficulties_ReturnsEmptyList() {
        // given
        List<String> difficulties = new ArrayList<>();

        // when
        List<ProblemDto> result = problemService.findByDifficultyIn(difficulties);

        // assert
        assertTrue(result.isEmpty());
    }
}