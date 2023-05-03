package com.example.backend.service;

import com.example.backend.dto.ProblemDto;
import com.example.backend.entity.Problem;
import com.example.backend.exceptions.CrudOperationException;
import com.example.backend.repository.ProblemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProblemService {
    private final ProblemRepository problemRepository;

    public ProblemService(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }

    public ProblemDto addProblem(ProblemDto problemDto) {
        Problem problem = Problem.builder()
                .valuesType(problemDto.getValuesType())
                .description(problemDto.getDescription())
                .valuesToCheckCode(problemDto.getValuesToCheckCode())
                .difficulty(problemDto.getDifficulty())
                .resultsToCheckCode(problemDto.getResultsToCheckCode())
                .build();

        problemRepository.save(problem);
        problemDto.setProblemId(problem.getProblemId());
        return problemDto;
    }

    public void removeProblem(Long id) {
        problemRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Problem does not exist");
        });

        problemRepository.deleteById(id);
    }

    public ProblemDto updateProblem(Long id, ProblemDto problemDto) {
        Problem problem = problemRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Problem does not exist");
        });
        problem.setDescription(problemDto.getDescription());
        problem.setDifficulty(problemDto.getDifficulty());
        problem.setValuesType(problemDto.getValuesType());
        problem.setValuesToCheckCode(problemDto.getValuesToCheckCode());
        problem.setResultsToCheckCode(problemDto.getResultsToCheckCode());
        problemRepository.save(problem);
        problemDto.setProblemId(problem.getProblemId());
        return problemDto;
    }

    public ProblemDto getProblemById(Long id) {
        Problem problem = problemRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Problem does not exist");
        });

        return ProblemDto.builder()
                .problemId(problem.getProblemId())
                .description(problem.getDescription())
                .difficulty(problem.getDifficulty())
                .valuesToCheckCode(problem.getValuesToCheckCode())
                .valuesType(problem.getValuesType())
                .resultsToCheckCode(problem.getResultsToCheckCode())
                .build();
    }

    public List<ProblemDto> getAllProblems() {
        Iterable<Problem> problems = problemRepository.findAll();
        List<ProblemDto> problemDtos = new ArrayList<>();

        problems.forEach(problem ->
                problemDtos.add(ProblemDto.builder()
                        .problemId(problem.getProblemId())
                        .valuesType(problem.getValuesType())
                        .valuesToCheckCode(problem.getValuesToCheckCode())
                        .difficulty(problem.getDifficulty())
                        .description(problem.getDescription())
                        .resultsToCheckCode(problem.getResultsToCheckCode())
                        .build()

                ));
        return problemDtos;
    }

}
