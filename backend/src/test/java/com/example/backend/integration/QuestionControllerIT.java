package com.example.backend.integration;

import com.example.backend.controller.QuestionController;
import com.example.backend.controller.QuizController;
import com.example.backend.dto.QuestionDto;
import com.example.backend.dto.QuizDto;
import com.example.backend.entity.Quiz;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class QuestionControllerIT {
    @Autowired
    private QuestionController questionController;

    @Autowired
    private QuizController quizController;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Quiz quiz;

    @BeforeEach
    public void setUp() {
        quiz = Quiz.builder()
                .name("test quiz")
                .noQuestions(10)
                .build();

        QuizDto quizDto = QuizDto.builder()
                .name("test quiz")
                .noQuestions(10)
                .build();

        QuizDto saved = quizController.addQuiz(quizDto).getBody();
        assertNotNull(saved);
        quiz.setQuizId(saved.getQuizId());
    }

    @Test
    @DirtiesContext
    void testQuestionIsPersistedCorrectlyThroughQuestionController() {
        QuestionDto questionDto = QuestionDto.builder()
                .quizId(quiz.getQuizId())
                .score(2)
                .description("test")
                .build();

        // add question
        QuestionDto saved = questionController.addQuestion(questionDto).getBody();
        assertNotNull(saved);

        // getQuestionById
        Long questionId = saved.getQuestionId();
        QuestionDto queriedQuestionDto = questionController.getQuestionById(questionId).getBody();
        assertNotNull(queriedQuestionDto);
        assertEquals(questionDto.getDescription(), queriedQuestionDto.getDescription());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "question"));

        // get questions
        List<QuestionDto> allQuestionsDtos = questionController.getAllQuestions().getBody();
        assertNotNull(allQuestionsDtos);
        assertEquals(1, allQuestionsDtos.size());

        // update question
        QuestionDto updateQuestionDto = QuestionDto.builder()
                .quizId(quiz.getQuizId())
                .score(2)
                .description("test")
                .build();
        QuestionDto savedUpdated = questionController.updateQuestion(saved.getQuestionId(), updateQuestionDto).getBody();
        assertNotNull(savedUpdated);
        QuestionDto updateQueriedQuestionDto = questionController.getQuestionById(questionId).getBody();
        assert updateQueriedQuestionDto != null;
        assertEquals(updateQuestionDto.getDescription(), updateQueriedQuestionDto.getDescription());

        // delete question
        questionController.removeQuestion(saved.getQuestionId());
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "question"));
    }

    @Test
    @DirtiesContext
    void testQuestionAdd1000QuestionsThroughQuestionController() {
        final int TIMES = 1000;

        for (int i = 0; i < TIMES; ++i) {
            QuestionDto quizDto = QuestionDto.builder().quizId(quiz.getQuizId()).questionId(i).build();

            // add question
            QuestionDto saved = questionController.addQuestion(quizDto).getBody();
            assertNotNull(saved);
        }
        assertEquals(TIMES, JdbcTestUtils.countRowsInTable(jdbcTemplate, "question"));

        // get questions
        List<QuestionDto> allQuestionDtos = questionController.getAllQuestions().getBody();
        assertNotNull(allQuestionDtos);
        assertEquals(TIMES, allQuestionDtos.size());
    }

    @Test
    @DirtiesContext
    void testQuestionAddQuestionWithSetIdThroughQuestionController() {
        QuestionDto questionDto = QuestionDto.builder()
                .quizId(quiz.getQuizId())
                .score(2)
                .description("test")
                .build();

        // add question
        QuestionDto saved = questionController.addQuestion(questionDto).getBody();
        assertNotNull(saved);

        QuestionDto questionDto1 = QuestionDto.builder()
                .questionId(questionDto.getQuestionId())
                .quizId(quiz.getQuizId())
                .score(2)
                .description("test")
                .build();
        // add question
        QuestionDto saved1 = questionController.addQuestion(questionDto1).getBody();
        assertNotNull(saved1);

        assertNotEquals(saved.getQuestionId(), saved1.getQuestionId());
    }
}
