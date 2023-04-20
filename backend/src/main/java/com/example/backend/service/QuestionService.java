package com.example.backend.service;

import com.example.backend.dto.QuestionDto;
import com.example.backend.entity.Question;
import com.example.backend.entity.Quiz;
import com.example.backend.exceptions.CrudOperationException;
import com.example.backend.repository.QuestionRepository;
import com.example.backend.repository.QuizRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    private final QuizRepository quizRepository;

    public QuestionService(QuestionRepository questionRepository, QuizRepository quizRepository) {
        this.questionRepository = questionRepository;
        this.quizRepository = quizRepository;
    }

    public QuestionDto addQuestion(QuestionDto questionDto) {
        Quiz quiz = quizRepository.findById(questionDto.getQuizId()).orElseThrow(() -> {
            throw new CrudOperationException("This quiz is not available!");
        });
        Question question = Question.builder()
                .quiz(quiz)
                .description(questionDto.getDescription())
                .score(questionDto.getScore())
                .build();
        questionRepository.save(question);
        questionDto.setQuestionId(question.getQuestionId());
        return questionDto;
    }

    public void removeQuestion(Long id) {
        questionRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Question does not exist");
        });

        questionRepository.deleteById(id);
    }

    public QuestionDto updateQuestion(Long id, QuestionDto questionDto) {
        Question question = questionRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Question does not exist");
        });

        Quiz quiz = quizRepository.findById(questionDto.getQuizId()).orElseThrow(() -> {
            throw new CrudOperationException("This quiz is not available!");
        });

        question.setDescription(questionDto.getDescription());
        question.setScore(questionDto.getScore());
        question.setQuiz(quiz);
        questionRepository.save(question);
        questionDto.setQuestionId(question.getQuestionId());
        return questionDto;
    }

    public QuestionDto getQuestionById(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Question does not exist");
        });

        return QuestionDto.builder()
                .questionId(question.getQuestionId())
                .quizId(question.getQuiz().getQuizId())
                .description(question.getDescription())
                .score(question.getScore())
                .build();
    }

    public List<QuestionDto> getAllQuestions() {
        Iterable<Question> questions = questionRepository.findAll();
        List<QuestionDto> questionDtos = new ArrayList<>();

        questions.forEach(question ->
                questionDtos.add(QuestionDto.builder()
                        .questionId(question.getQuestionId())
                        .quizId(question.getQuiz().getQuizId())
                        .description(question.getDescription())
                        .score(question.getScore())
                        .build())
        );
        return questionDtos;
    }
}
