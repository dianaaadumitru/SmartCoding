package com.example.backend.service;

import com.example.backend.dto.ProblemDto;
import com.example.backend.dto.UserSolutionForProblemDto;
import com.example.backend.entity.Problem;
import com.example.backend.entity.UserProblemResults;
import com.example.backend.entity.enums.Difficulty;
import com.example.backend.entity.enums.ReturnType;
import com.example.backend.exceptions.CrudOperationException;
import com.example.backend.repository.ProblemRepository;
import com.example.backend.repository.UserProblemResultsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProblemService {
    private final ProblemRepository problemRepository;

    private final UserProblemResultsRepository userProblemResultsRepository;

    public ProblemService(ProblemRepository problemRepository, UserProblemResultsRepository userProblemResultsRepository) {
        this.problemRepository = problemRepository;
        this.userProblemResultsRepository = userProblemResultsRepository;
    }

    public ProblemDto addProblem(ProblemDto problemDto) {
        System.out.println(Difficulty.valueOf(problemDto.getDifficulty()));
        Problem problem = Problem.builder()
                .valuesType(problemDto.getValuesType())
                .name(problemDto.getName())
                .description(problemDto.getDescription())
                .valuesToCheckCode(problemDto.getValuesToCheckCode())
                .difficulty(Difficulty.valueOf(problemDto.getDifficulty()))
                .resultsToCheckCode(problemDto.getResultsToCheckCode())
                .returnType(ReturnType.valueOf(problemDto.getReturnType()))
                .noParameters(problemDto.getNoParameters())
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
        problem.setName(problemDto.getName());
        problem.setDescription(problemDto.getDescription());
        problem.setDifficulty(Difficulty.valueOf(problemDto.getDifficulty()));
        problem.setValuesType(problemDto.getValuesType());
        problem.setValuesToCheckCode(problemDto.getValuesToCheckCode());
        problem.setResultsToCheckCode(problemDto.getResultsToCheckCode());
        problem.setReturnType(ReturnType.valueOf(problemDto.getReturnType()));
        problem.setNoParameters(problemDto.getNoParameters());
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
                .name(problem.getName())
                .description(problem.getDescription())
                .difficulty(problem.getDifficulty().toString())
                .valuesToCheckCode(problem.getValuesToCheckCode())
                .valuesType(problem.getValuesType())
                .resultsToCheckCode(problem.getResultsToCheckCode())
                .returnType(problem.getReturnType().toString())
                .noParameters(problem.getNoParameters())
                .build();
    }

    public List<ProblemDto> getAllProblems() {
        Iterable<Problem> problems = problemRepository.findAll();
        List<ProblemDto> problemDtos = new ArrayList<>();

        problems.forEach(problem ->
                problemDtos.add(ProblemDto.builder()
                        .problemId(problem.getProblemId())
                        .name(problem.getName())
                        .valuesType(problem.getValuesType())
                        .valuesToCheckCode(problem.getValuesToCheckCode())
                        .difficulty(problem.getDifficulty().toString())
                        .description(problem.getDescription())
                        .resultsToCheckCode(problem.getResultsToCheckCode())
                        .returnType(problem.getReturnType().toString())
                        .noParameters(problem.getNoParameters())
                        .build()

                ));
        return problemDtos;
    }

    public List<UserSolutionForProblemDto> getAllUsersSolutionsForProblem(Long problemId) {
        List<UserProblemResults> userProblemResults = userProblemResultsRepository.findByProblem_ProblemId(problemId);
        return userProblemResults.stream().map((userProblemResult -> UserSolutionForProblemDto.builder()
                .userLastName(userProblemResult.getUser().getLastName())
                .userFirsName(userProblemResult.getUser().getFirstName())
                .score(userProblemResult.getPercentage())
                .build())).toList();
    }

    public List<ProblemDto> findByDifficultyIn(List<String> difficulties) {
        var diff = difficulties.stream().map(Difficulty::valueOf).toList();
        List<Problem> problems = problemRepository.findByDifficultyIn(diff);

        return problems.stream().map(problem -> ProblemDto.builder()
                .problemId(problem.getProblemId())
                .name(problem.getName())
                .valuesType(problem.getValuesType())
                .valuesToCheckCode(problem.getValuesToCheckCode())
                .difficulty(problem.getDifficulty().toString())
                .description(problem.getDescription())
                .resultsToCheckCode(problem.getResultsToCheckCode())
                .returnType(problem.getReturnType().toString())
                .noParameters(problem.getNoParameters())
                .build()).toList();

    }


}
