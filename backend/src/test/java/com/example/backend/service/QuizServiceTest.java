package com.example.backend.service;

import com.example.backend.dto.QuizDto;
import com.example.backend.entity.Quiz;
import com.example.backend.repository.QuizRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuizServiceTest {
    @Mock
    private QuizRepository quizRepository;

    @InjectMocks
    private QuizService quizService;

    @Test
    public void givenQuizDto_addQuiz_returnEqualQuizDto() {
        // given
        QuizDto quizDto = QuizDto.builder()
                .name("test")
                .noQuestions(4)
                .build();

        Quiz quiz = Quiz.builder()
                .noQuestions(quizDto.getNoQuestions())
                .name(quizDto.getName())
                .build();

        when(quizRepository.save(ArgumentMatchers.any(Quiz.class))).thenReturn(quiz);

        // when
        QuizDto created = quizService.addQuiz(quizDto);

        // then
        assertEquals(created, quizDto);
        verify(quizRepository).save(ArgumentMatchers.any(Quiz.class));
    }

    @Test
    public void givenAllQuizzes_getQuizzes_returnAllQuizzes() {
        // given
        List<Quiz> quizzes = new ArrayList<>();
        quizzes.add(Quiz.builder().name("test").build());

        when(quizRepository.findAll()).thenReturn(quizzes);

        // when
        List<QuizDto> expected = quizService.getAllQuizzes();
        List<QuizDto> quizDtos = quizzes.stream().map(quiz ->
                QuizDto.builder()
                        .name(quiz.getName())
                        .noQuestions(quiz.getNoQuestions())
                        .build()).toList();

        // then
        assertEquals(expected, quizDtos);
        verify(quizRepository).findAll();
    }

    @Test
    public void givenQuiz_removeQuiz_deleteQuizIfFound() {
        // given
        Quiz quiz = Quiz.builder()
                .quizId(1L)
                .name("test")
                .build();

        when(quizRepository.findById(quiz.getQuizId())).thenReturn(Optional.of(quiz));

        // when
        quizService.removeQuiz(quiz.getQuizId());

        // then
        verify(quizRepository).deleteById(quiz.getQuizId());
    }

    @Test
    public void givenQuiz_removeQuiz_throwExceptionIfQuizDoesntExist() {
        // given
        Quiz quiz = Quiz.builder()
                .quizId(1L)
                .name("test")
                .build();

        when(quizRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> quizService.removeQuiz(quiz.getQuizId());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenQuizAndQuizDto_updateQuiz_updateQuizIfFound() {
        // given
        Quiz quiz = Quiz.builder()
                .quizId(10L)
                .name("test")
                .build();

        QuizDto newQuizDto = QuizDto.builder()
                .name("new name").build();

        when(quizRepository.findById(quiz.getQuizId())).thenReturn(Optional.of(quiz));

        // when
        quizService.updateQuiz(quiz.getQuizId(), newQuizDto);

        // then
        assertEquals(newQuizDto.getName(), quiz.getName());
        verify(quizRepository).save(ArgumentMatchers.any(Quiz.class));
        verify(quizRepository).findById(quiz.getQuizId());
    }

    @Test
    public void givenQuizAndQuizDto_updateQuiz_throwExceptionIfQuizDoesntExist() {
        // given
        Quiz quiz = Quiz.builder()
                .quizId(10L)
                .name("test")
                .build();

        QuizDto newQuizDto = QuizDto.builder()
                .name("new name").build();

        when(quizRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> quizService.updateQuiz(quiz.getQuizId(), newQuizDto);

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenQuiz_findQuizById_returnEqualQuizIfFound() {
        // given
        Quiz quiz = Quiz.builder()
                .quizId(10L)
                .name("test")
                .build();

        when(quizRepository.findById(quiz.getQuizId())).thenReturn(Optional.of(quiz));

        // when
        QuizDto expectedDto = quizService.getQuizById(quiz.getQuizId());
        QuizDto actualDto = QuizDto.builder()
                .name(quiz.getName())
                .noQuestions(quiz.getNoQuestions())
                .quizId(quiz.getQuizId())
                .build();

        // then
        assertEquals(actualDto, expectedDto);
        verify(quizRepository).findById(quiz.getQuizId());
    }

    @Test
    public void givenQuiz_findQuizById_throwExceptionIfQuizDoesntExist() {
        // given
        Quiz quiz = Quiz.builder()
                .quizId(10L)
                .name("test")
                .build();

        when(quizRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> quizService.getQuizById(quiz.getQuizId());

        // then
        assertThrows(RuntimeException.class, call);
    }
}
