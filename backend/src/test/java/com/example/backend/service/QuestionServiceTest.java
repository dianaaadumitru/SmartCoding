package com.example.backend.service;

import com.example.backend.dto.QuestionDto;
import com.example.backend.entity.Question;
import com.example.backend.entity.Quiz;
import com.example.backend.repository.QuestionRepository;
import com.example.backend.repository.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
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
public class QuestionServiceTest {
    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    @Mock
    private QuizRepository quizRepository;

    private Quiz quiz;

    @BeforeEach
    public void setUp() {
        quiz = Quiz.builder()
                .quizId(1L)
                .name("test quiz")
                .noQuestions(10)
                .build();
    }

    @Test
    public void givenQuestionDto_addQuestion_returnEqualQuestionDto() {
        // given
        QuestionDto questionDto = QuestionDto.builder()
                .quizId(quiz.getQuizId())
                .score(2)
                .description("test")
                .build();

        Question question = Question.builder()
                .quiz(quiz)
                .score(questionDto.getScore())
                .description(questionDto.getDescription())
                .build();

        when(quizRepository.findById(quiz.getQuizId())).thenReturn(Optional.of(quiz));
        when(questionRepository.save(ArgumentMatchers.any(Question.class))).thenReturn(question);

        // when
        QuestionDto newQuestionDto = questionService.addQuestion(questionDto);

        // then
        assertEquals(questionDto, newQuestionDto);
        verify(questionRepository).save(ArgumentMatchers.any(Question.class));
        verify(quizRepository).findById(quiz.getQuizId());
    }

    @Test
    public void givenAllQuestions_getQuestions_returnAllQuestions() {
        // given
        List<Question> questions = new ArrayList<>();
        questions.add(Question.builder()
                .description("test")
                .score(4)
                .quiz(quiz)
                .build());

        when(questionRepository.findAll()).thenReturn(questions);

        // when
        List<QuestionDto> expected = questionService.getAllQuestions();
        List<QuestionDto> actual = questions.stream().map(question ->
                QuestionDto.builder()
                        .quizId(question.getQuiz().getQuizId())
                        .score(question.getScore())
                        .description(question.getDescription())
                        .build()).toList();

        // then
        assertEquals(expected, actual);
        verify(questionRepository).findAll();
    }

    @Test
    public void givenQuestion_removeQuestion_deleteQuestionIfFound() {
        // given
        Question question = Question.builder()
                .questionId(1L)
                .quiz(quiz)
                .score(2)
                .description("test")
                .build();

        when(questionRepository.findById(question.getQuestionId())).thenReturn(Optional.of(question));

        // when
        questionService.removeQuestion(question.getQuestionId());

        // then
        verify(questionRepository).deleteById(question.getQuestionId());
    }

    @Test
    public void givenQuestion_removeQuestion_throwExceptionIfQuestionDoesntExist() {
        // given
        Question question = Question.builder()
                .questionId(1L)
                .quiz(quiz)
                .score(2)
                .description("test")
                .build();

        when(questionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> questionService.removeQuestion(question.getQuestionId());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenQuestionAndQuestionDto_updateQuestion_updateQuestionIfFound() {
        // given
        Question question = Question.builder()
                .questionId(1L)
                .quiz(quiz)
                .score(2)
                .description("test")
                .build();

        QuestionDto newQuestionDto = QuestionDto.builder()
                .quizId(quiz.getQuizId())
                .score(2)
                .description("test update")
                .build();

        when(questionRepository.findById(question.getQuestionId())).thenReturn(Optional.of(question));
        when(quizRepository.findById(quiz.getQuizId())).thenReturn(Optional.of(quiz));

        // when
        questionService.updateQuestion(question.getQuestionId(), newQuestionDto);

        // then
        assertEquals(question.getDescription(), newQuestionDto.getDescription());
        verify(questionRepository).save(ArgumentMatchers.any(Question.class));
        verify(questionRepository).findById(question.getQuestionId());
        verify(quizRepository).findById(quiz.getQuizId());
    }

    @Test
    public void givenQuestionAndQuestionDto_updateQuestion_throwExceptionIfQuestionDoesntExist() {
        // given
        Question question = Question.builder()
                .questionId(1L)
                .quiz(quiz)
                .score(2)
                .description("test")
                .build();

        QuestionDto newQuestionDto = QuestionDto.builder()
                .quizId(quiz.getQuizId())
                .score(2)
                .description("test update")
                .build();

        when(questionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> questionService.updateQuestion(question.getQuestionId(), newQuestionDto);

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenQuestionAndQuestionDto_updateQuestion_throwExceptionIfQuizDoesntExist() {
        // given
        Question question = Question.builder()
                .questionId(1L)
                .quiz(quiz)
                .score(2)
                .description("test")
                .build();

        QuestionDto newQuestionDto = QuestionDto.builder()
                .quizId(quiz.getQuizId())
                .score(2)
                .description("test update")
                .build();

        when(questionRepository.findById(question.getQuestionId())).thenReturn(Optional.of(question));
        when(quizRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> questionService.updateQuestion(question.getQuestionId(), newQuestionDto);

        // then
        assertThrows(RuntimeException.class, call);
        verify(questionRepository).findById(question.getQuestionId());
    }

    @Test
    public void givenQuestion_findQuestionById_returnEqualQuizIfFound() {
        // given
        Question question = Question.builder()
                .questionId(1L)
                .quiz(quiz)
                .score(2)
                .description("test")
                .build();

        when(questionRepository.findById(question.getQuestionId())).thenReturn(Optional.of(question));

        // when
        QuestionDto expected = questionService.getQuestionById(question.getQuestionId());
        QuestionDto actual = QuestionDto.builder()
                .questionId(question.getQuestionId())
                .quizId(question.getQuiz().getQuizId())
                .score(question.getScore())
                .description(question.getDescription())
                .build();

        // then
        assertEquals(expected, actual);
        verify(questionRepository).findById(question.getQuestionId());
    }

    @Test
    public void givenQuestion_findQuestionById_throwExceptionIfQuestionDoesntExist() {
        // given
        Question question = Question.builder()
                .questionId(1L)
                .quiz(quiz)
                .score(2)
                .description("test")
                .build();

        when(questionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> questionService.getQuestionById(question.getQuestionId());

        // then
        assertThrows(RuntimeException.class, call);
    }
}
