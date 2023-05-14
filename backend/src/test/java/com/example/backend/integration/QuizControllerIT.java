package com.example.backend.integration;

import com.example.backend.controller.QuizController;
import com.example.backend.dto.QuizDto;
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
public class QuizControllerIT {
    @Autowired
    private QuizController quizController;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DirtiesContext
    void testQuizIsPersistedCorrectlyThroughQuizController() {
        QuizDto quizDto = QuizDto.builder()
                .name("test")
                .noQuestions(4)
                .build();

        // add quiz
        QuizDto saved = quizController.addQuiz(quizDto).getBody();
        assertNotNull(saved);

        // getQuizById
        Long quizId = saved.getQuizId();
        QuizDto queriedQuizDto = quizController.getQuizById(quizId).getBody();
        assertNotNull(queriedQuizDto);
        assertEquals(quizDto.getName(), queriedQuizDto.getName());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "quiz"));

        // get quizzes
        List<QuizDto> allQuizzesDtos = quizController.getAllQuizs().getBody();
        assertNotNull(allQuizzesDtos);
        assertEquals(1, allQuizzesDtos.size());

        // update quiz
        QuizDto updateQuizDto = QuizDto.builder()
                .name("test update")
                .build();
        QuizDto savedUpdated = quizController.updateQuiz(saved.getQuizId(), updateQuizDto).getBody();
        assertNotNull(savedUpdated);
        QuizDto updateQueriedQuizDto = quizController.getQuizById(quizId).getBody();
        assert updateQueriedQuizDto != null;
        assertEquals(updateQuizDto.getName(), updateQueriedQuizDto.getName());

        // delete quiz
        quizController.removeQuiz(saved.getQuizId());
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "quiz"));
    }

    @Test
    @DirtiesContext
    void testQuizAdd1000QuizsThroughQuizController() {
        final int TIMES = 1000;

        for (int i = 0; i < TIMES; ++i) {
            QuizDto quizDto = QuizDto.builder().quizId(i).build();

            // add quiz
            QuizDto saved = quizController.addQuiz(quizDto).getBody();
            assertNotNull(saved);
        }
        assertEquals(TIMES, JdbcTestUtils.countRowsInTable(jdbcTemplate, "quiz"));

        // get quizzes
        List<QuizDto> allQuizDtos = quizController.getAllQuizs().getBody();
        assertNotNull(allQuizDtos);
        assertEquals(TIMES, allQuizDtos.size());
    }

    @Test
    @DirtiesContext
    void testQuizAddQuizWithSetIdThroughQuizController() {
        QuizDto quizDto = QuizDto.builder()
                .name("test")
                .noQuestions(4)
                .quizId(10L)
                .build();

        // add quiz
        QuizDto saved = quizController.addQuiz(quizDto).getBody();
        assertNotNull(saved);

        QuizDto quizDto1 = QuizDto.builder()
                .name("test1")
                .noQuestions(4)
                .quizId(quizDto.getQuizId())
                .build();
        // add quiz
        QuizDto saved1 = quizController.addQuiz(quizDto1).getBody();
        assertNotNull(saved1);

        assertNotEquals(saved.getQuizId(), saved1.getQuizId());
    }
}
