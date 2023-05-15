package com.example.backend.service;

import com.example.backend.dto.LessonDto;
import com.example.backend.dto.ProblemDto;
import com.example.backend.entity.Lesson;
import com.example.backend.entity.Problem;
import com.example.backend.exceptions.CrudOperationException;
import com.example.backend.repository.LessonRepository;
import com.example.backend.repository.ProblemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LessonService {
    private final LessonRepository lessonRepository;

    private final ProblemRepository problemRepository;

    public LessonService(LessonRepository lessonRepository, ProblemRepository problemRepository) {
        this.lessonRepository = lessonRepository;
        this.problemRepository = problemRepository;
    }

    public LessonDto addLesson(LessonDto lessonDto) {
        Lesson lesson = Lesson.builder()
                .name(lessonDto.getName())
                .description(lessonDto.getDescription())
                .expectedTime(lessonDto.getExpectedTime())
                .build();

        lessonRepository.save(lesson);
        lessonDto.setId(lesson.getLessonId());
        return lessonDto;
    }

    public void removeLesson(Long id) {
        lessonRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Lesson does not exist!");
        });
        lessonRepository.deleteById(id);
    }

    public LessonDto updateLesson(Long id, LessonDto lessonDto) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Lesson does not exist!");
        });

        lesson.setName(lessonDto.getName());
        lesson.setDescription(lessonDto.getDescription());
        lesson.setExpectedTime(lessonDto.getExpectedTime());

        lessonRepository.save(lesson);
        lessonDto.setId(lessonDto.getId());
        return lessonDto;
    }

    public LessonDto getLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Lesson does not exist!");
        });

        return LessonDto.builder()
                .id(lesson.getLessonId())
                .name(lesson.getName())
                .description(lesson.getDescription())
                .expectedTime(lesson.getExpectedTime())
                .build();
    }

    public List<LessonDto> getAllLessons() {
        Iterable<Lesson> lessons = lessonRepository.findAll();
        List<LessonDto> lessonDtos = new ArrayList<>();

        lessons.forEach(lesson ->
                lessonDtos.add(LessonDto.builder()
                        .id(lesson.getLessonId())
                        .name(lesson.getName())
                        .description(lesson.getDescription())
                        .expectedTime(lesson.getExpectedTime())
                        .build())
        );

        return lessonDtos;
    }

    @Transactional
    public ProblemDto addProblemToLesson(Long lessonId, Long problemId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> {
            throw new CrudOperationException("Lesson does not exist!");
        });

        Problem problem = problemRepository.findById(problemId).orElseThrow(() -> {
            throw new CrudOperationException("Problem does not exist!");
        });

        if (lesson.getProblems() == null || lesson.getProblems().isEmpty()) {
            lesson.setProblems(new ArrayList<>());
        }

        problem.setLesson(lesson);
        lesson.getProblems().add(problem);
        lessonRepository.save(lesson);


        return ProblemDto.builder()
                .problemId(problem.getProblemId())
                .valuesType(problem.getValuesType())
                .valuesToCheckCode(problem.getValuesToCheckCode())
                .difficulty(problem.getDifficulty())
                .description(problem.getDescription())
                .resultsToCheckCode(problem.getResultsToCheckCode())
                .returnType(problem.getReturnType().toString())
                .build();
    }

    @Transactional
    public void removeProblemFromLesson(long lessonId, long problemId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> {
            throw new CrudOperationException("Lesson does not exist!");
        });

        Problem problem = problemRepository.findById(problemId).orElseThrow(() -> {
            throw new CrudOperationException("Problem does not exist!");
        });

        lesson.getProblems().remove(problem);
        problem.setLesson(null);
        problemRepository.save(problem);
        lessonRepository.save(lesson);
    }


    public List<ProblemDto> getAllLessonProblems(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> {
            throw new CrudOperationException("Lesson does not exist!");
        });

        System.out.println(lesson.getLessonId());

        List<Problem> problems = lesson.getProblems();
        System.out.println("in retrieve: " + problems.size());

        return problems.stream().map(problem ->
                ProblemDto.builder()
                        .problemId(problem.getProblemId())
                        .valuesType(problem.getValuesType())
                        .valuesToCheckCode(problem.getValuesToCheckCode())
                        .difficulty(problem.getDifficulty())
                        .description(problem.getDescription())
                        .resultsToCheckCode(problem.getResultsToCheckCode())
                        .returnType(problem.getReturnType().toString())
                        .build()).toList();
    }
}
