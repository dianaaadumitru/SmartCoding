package com.example.backend.service;

import com.example.backend.dto.*;
import com.example.backend.entity.*;
import com.example.backend.exceptions.CrudOperationException;
import com.example.backend.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    private final QuestionRepository questionRepository;

    private final UserResultsRepository userResultsRepository;

    private final RoleRepository roleRepository;

    private final ProblemRepository problemRepository;

    private final UserProblemResultsRepository userProblemResultsRepository;


    public UserService(UserRepository userRepository, UserResultsRepository userResultsRepository, QuestionRepository questionRepository, RoleRepository roleRepository, ProblemRepository problemRepository, UserProblemResultsRepository userProblemResultsRepository) {
        this.userRepository = userRepository;
        this.userResultsRepository = userResultsRepository;
        this.questionRepository = questionRepository;
        this.roleRepository = roleRepository;
        this.problemRepository = problemRepository;
        this.userProblemResultsRepository = userProblemResultsRepository;
    }

    public UserDto addUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new CrudOperationException("Email already taken!");
        }

        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new CrudOperationException("username already taken!");
        }

        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .build();
        userRepository.save(user);
        userDto.setUserId(user.getUserId());
        return userDto;
    }

    public void removeUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });
        userRepository.deleteById(id);
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setUsername(userDto.getUsername());
        userRepository.save(user);
        userDto.setUserId(user.getUserId());
        return userDto;
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });

        UserDto userDto = UserDto.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
        if (user.getRoles() != null && user.getRoles().size() > 0) {
            userDto.setUserType(user.getRoles().iterator().next().getRole());
        }
        return userDto;
    }

    public List<UserDto> getAllUsers() {
        Iterable<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();

        users.forEach(user ->
                {
                    UserDto userDto = UserDto.builder()
                            .userId(user.getUserId())
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .email(user.getEmail())
                            .username(user.getUsername())
                            .password(user.getPassword())
                            .testResult(user.getTestResult())
                            .build();
                    if (user.getRoles() != null && user.getRoles().size() > 0) {
                        userDto.setUserType(user.getRoles().iterator().next().getRole());
                    }
                    userDtos.add(userDto);
                }
        );
        return userDtos;
    }

    public UserDto findUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });

        UserDto userDto = UserDto.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

        if (user.getRoles() != null && user.getRoles().size() > 0) {
            userDto.setUserType(user.getRoles().iterator().next().getRole());
        }
        return userDto;
    }

    public UserDto findUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });

        UserDto userDto = UserDto.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

        if (user.getRoles() != null && user.getRoles().size() > 0) {
            userDto.setUserType(user.getRoles().iterator().next().getRole());
        }
        return userDto;
    }

    public UserDto findUserByUsernameOrEmail(String username, String email) {
        User user = userRepository.findByUsernameOrEmail(username, email).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });

        UserDto userDto = UserDto.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

        if (user.getRoles() != null && user.getRoles().size() > 0) {
            userDto.setUserType(user.getRoles().iterator().next().getRole());
        }
        return userDto;
    }

    public UserDto assignRoleToUser(Long userId, Long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> {
            throw new CrudOperationException("Role does not exist!");
        });

        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });

        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        } else {
            throw new CrudOperationException("this user already has a role!");
        }

        user.getRoles().add(role);
        userRepository.save(user);

        return UserDto.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .userType(role.getRole())
                .build();
    }

    @Transactional
    public UserResultsDto addAnswerAndQuestionScoreToStudent(Long userId, Long questionId, double score, String answer) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });

        Question question = questionRepository.findById(questionId).orElseThrow(() -> {
            throw new CrudOperationException("Question does not exist");
        });

        if (user.getUserResults() == null) {
            user.setUserResults(new ArrayList<>());
        }

        UserResults userResult = UserResults.builder()
                .userQuestionId(new UserQuestionId(userId, questionId))
                .user(user)
                .question(question)
                .score(score)
                .answer(answer)
                .build();

        userResultsRepository.save(userResult);

        return UserResultsDto.builder()
                .userId(userId)
                .userFirstName(user.getFirstName())
                .userLastName(user.getLastName())
                .questionId(questionId)
                .questionDescription(question.getDescription())
                .score(score)
                .answer(userResult.getAnswer())
                .build();
    }

    @Transactional
    public UserProblemResultDto addAnswerAndProblemPercentageToStudent(Long userId, Long problemId, int percentage, String answer) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });

        Problem problem = problemRepository.findById(problemId).orElseThrow(() -> {
            throw new CrudOperationException("Problem does not exist");
        });

        if (user.getUserProblemResults() == null) {
            user.setUserProblemResults(new ArrayList<>());
        }

        UserProblemResults userProblemResult = UserProblemResults.builder()
                .user(user)
                .problem(problem)
                .percentage(percentage)
                .answer(answer)
                .build();

        userProblemResultsRepository.save(userProblemResult);

        return UserProblemResultDto.builder()
                .userId(userId)
                .userFirstName(user.getFirstName())
                .userLastName(user.getLastName())
                .problemId(problemId)
                .problemDescription(problem.getDescription())
                .percentage(userProblemResult.getPercentage())
                .answer(userProblemResult.getAnswer())
                .build();
    }

    public List<UserResultsDto> getQuestionsResultsForStudent(Long userId) {
        List<UserResults> userResults = userResultsRepository.findByUser_UserId(userId);
        return userResults.stream().map((userResult -> UserResultsDto.builder()
                        .userId(userResult.getUser().getUserId())
                        .userFirstName(userResult.getUser().getFirstName())
                        .userLastName(userResult.getUser().getLastName())
                        .questionId(userResult.getQuestion().getQuestionId())
                        .questionDescription(userResult.getQuestion().getDescription())
                        .score(userResult.getScore())
                        .answer(userResult.getAnswer())
                        .build()))
                .collect(Collectors.toList());
    }

    public List<UserProblemResultDto> getProblemsResultsForStudent(Long userId) {
        List<UserProblemResults> userProblemResults = userProblemResultsRepository.findByUser_UserId(userId);
        return userProblemResults.stream().map((userProblemResult -> UserProblemResultDto.builder()
                        .userId(userProblemResult.getUser().getUserId())
                        .userFirstName(userProblemResult.getUser().getFirstName())
                        .userLastName(userProblemResult.getUser().getLastName())
                        .problemId(userProblemResult.getProblem().getProblemId())
                        .problemDescription(userProblemResult.getProblem().getDescription())
                        .percentage(userProblemResult.getPercentage())
                        .answer(userProblemResult.getAnswer())
                        .build()))
                .toList();
    }

    public UserTestResultDto computeTestScoreForUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });

        List<UserResults> userResults = userResultsRepository.findByUser_UserId(userId);
        double sum = 0;
        for (UserResults result : userResults) {
            sum += result.getScore();
        }

        user.setTestResult(sum);
        userRepository.save(user);

        return UserTestResultDto.builder()
                .userId(userId)
                .userFirstName(user.getFirstName())
                .userLastName(user.getLastName())
                .testResult(sum)
                .build();
    }

    public List<UserAndFinalScoreDto> getAllUsersFinalScores() {
        Iterable<User> users = userRepository.findAll();
        List<UserAndFinalScoreDto> userAndFinalScoreDtos = new ArrayList<>();

        users.forEach(user -> {
            UserAndFinalScoreDto userAndFinalScoreDto = UserAndFinalScoreDto.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .userId(user.getUserId())
                    .testResult(user.getTestResult())
                    .build();
            userAndFinalScoreDtos.add(userAndFinalScoreDto);
        });
        return userAndFinalScoreDtos;
    }
}
