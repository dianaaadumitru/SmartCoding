package com.example.backend.service;

import com.example.backend.dto.QuizDto;
import com.example.backend.entity.Quiz;
import com.example.backend.exceptions.CrudOperationException;
import com.example.backend.repository.QuizRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {
    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public QuizDto addQuiz(QuizDto quizDto) {
        Quiz quiz = Quiz.builder()
                .name(quizDto.getName())
                .noQuestions(quizDto.getNoQuestions())
                .build();
        quizRepository.save(quiz);
        quizDto.setQuizId(quiz.getQuizId());
        return quizDto;
    }

    public void removeQuiz(Long id) {
        quizRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Quiz does not exist");
        });

        quizRepository.deleteById(id);
    }

    public QuizDto updateQuiz(Long id, QuizDto quizDto) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Quiz does not exist");
        });

        quiz.setName(quizDto.getName());
        quiz.setNoQuestions(quizDto.getNoQuestions());
        quizRepository.save(quiz);
        quizDto.setQuizId(quiz.getQuizId());
        return quizDto;
    }

    public QuizDto getQuizById(Long id) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Quiz does not exist");
        });

        return QuizDto.builder()
                .quizId(id)
                .name(quiz.getName())
                .noQuestions(quiz.getNoQuestions())
                .build();
    }

    public List<QuizDto> getAllQuizzes() {
        Iterable<Quiz> quizzes = quizRepository.findAll();
        List<QuizDto> quizDtos = new ArrayList<>();

        quizzes.forEach(quiz ->
                quizDtos.add(QuizDto.builder()
                        .quizId(quiz.getQuizId())
                        .name(quiz.getName())
                        .noQuestions(quiz.getNoQuestions())
                        .build())
        );
        return quizDtos;
    }
}
