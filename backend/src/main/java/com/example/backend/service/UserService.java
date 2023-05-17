package com.example.backend.service;

import com.example.backend.dto.*;
import com.example.backend.entity.*;
import com.example.backend.exceptions.CrudOperationException;
import com.example.backend.repository.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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

    private final CourseRepository courseRepository;


    public UserService(UserRepository userRepository, UserResultsRepository userResultsRepository, QuestionRepository questionRepository, RoleRepository roleRepository, ProblemRepository problemRepository, UserProblemResultsRepository userProblemResultsRepository, UserProblemResultsRepository userProblemResultsRepository1, CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.userResultsRepository = userResultsRepository;
        this.questionRepository = questionRepository;
        this.roleRepository = roleRepository;
        this.problemRepository = problemRepository;
        this.userProblemResultsRepository = userProblemResultsRepository1;
        this.courseRepository = courseRepository;
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

        log.info(user.getRoles().toString());

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
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
    public UserResultsDto addQuestionAnswerAndQuestionScoreToStudent(Long userId, Long questionId, ScoreAnswerDto scoreAnswerDto) {
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
                .score(scoreAnswerDto.getScore())
                .answer(scoreAnswerDto.getAnswer())
                .build();

        userResultsRepository.save(userResult);

        return UserResultsDto.builder()
                .userId(userId)
                .userFirstName(user.getFirstName())
                .userLastName(user.getLastName())
                .questionId(questionId)
                .questionDescription(question.getDescription())
                .score(userResult.getScore())
                .answer(userResult.getAnswer())
                .build();
    }

    @Transactional
    public UserProblemResultDto addAnswerAndProblemPercentageToStudent(Long userId, Long problemId, ScoreAnswerDto scoreAnswerDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });

        Problem problem = problemRepository.findById(problemId).orElseThrow(() -> {
            throw new CrudOperationException("Problem does not exist");
        });

        if (user.getUserProblemResults() == null) {
            user.setUserProblemResults(new ArrayList<>());
        }
        log.info("im here");

        UserProblemResults userProblemResult = UserProblemResults.builder()
                .userProblemId(new UserProblemId(userId, problemId))
                .user(user)
                .problem(problem)
                .percentage(scoreAnswerDto.getScore())
                .answer(scoreAnswerDto.getAnswer())
                .build();

        log.info("created user-problem result: " + userProblemResult);

        userProblemResultsRepository.save(userProblemResult);

        log.info("saved it :)");

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

    public List<ProblemDto> getAllProblemsSolvedByAUser(Long userId) {
        List<UserProblemResults> userProblemResults = userProblemResultsRepository.findByUser_UserId(userId);

        return userProblemResults.stream().map(userProblemResult -> ProblemDto.builder()
                .problemId(userProblemResult.getProblem().getProblemId())
                .name(userProblemResult.getProblem().getName())
                .difficulty(userProblemResult.getProblem().getDifficulty().toString())
                .valuesToCheckCode(userProblemResult.getProblem().getValuesToCheckCode())
                .description(userProblemResult.getProblem().getDescription())
                .resultsToCheckCode(userProblemResult.getProblem().getResultsToCheckCode())
                .returnType(userProblemResult.getProblem().getReturnType().toString())
                .valuesType(userProblemResult.getProblem().getValuesType())
                .build()).toList();
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

    public List<CourseDto> findTop8Courses() {
        List<Course> courses = userRepository.findTop8Courses();
        return courses.stream().map(course ->
                CourseDto.builder()
                        .id(course.getCourseId())
                        .name(course.getName())
                        .description(course.getDescription())
                        .difficulty(course.getDifficulty().toString())
                        .courseType(course.getCourseTypes().iterator().next().getType())
                        .build()).toList();
    }

    public List<ProblemDto> findTop8Problems() {
        List<Problem> problems = userRepository.findTop8Problems();

        return problems.stream().map(problem ->
                ProblemDto.builder()
                        .problemId(problem.getProblemId())
                        .name(problem.getName())
                        .description(problem.getDescription())
                        .difficulty(problem.getDifficulty().toString())
                        .valuesToCheckCode(problem.getValuesToCheckCode())
                        .valuesType(problem.getValuesType())
                        .resultsToCheckCode(problem.getResultsToCheckCode())
                        .returnType(problem.getReturnType().toString())
                        .returnType(problem.getReturnType().toString())
                        .build()
                ).toList();
    }

    @Transactional
    public CourseDto addCourseToStudent(Long userId, Long courseId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });

        Course course = courseRepository.findById(courseId).orElseThrow(() -> {
            throw new CrudOperationException("Course does not exist!");
        });

        if (user.getCourses() == null || user.getCourses().isEmpty()) {
            user.setCourses(new ArrayList<>());
        }

        user.getCourses().add(course);

        userRepository.save(user);

        return CourseDto.builder()
                .id(course.getCourseId())
                .name(course.getName())
                .description(course.getDescription())
                .difficulty(course.getDifficulty().toString())
                .courseType(course.getCourseTypes().iterator().next().getType())
                .build();
    }

    public void removeCourseFromStudent(Long userId, Long courseId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });

        Course course = courseRepository.findById(courseId).orElseThrow(() -> {
            throw new CrudOperationException("Course does not exist!");
        });
        List<Course> courses = user.getCourses();

        if (!CollectionUtils.isEmpty(courses)) {
            courses.removeIf(course1 -> courseId.equals(course1.getCourseId()));
        }
        userRepository.save(user);
    }

    @Transactional
    public List<CourseDto> getUserCourses(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });

        List<Course> courses = user.getCourses();

        return courses.stream().map(course ->
                CourseDto.builder()
                        .id(course.getCourseId())
                        .name(course.getName())
                        .description(course.getDescription())
                        .difficulty(course.getDifficulty().toString())
                        .courseType(course.getCourseTypes().iterator().next().getType())
                        .build()).toList();
    }
}
