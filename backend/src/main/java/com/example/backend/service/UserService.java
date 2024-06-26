package com.example.backend.service;

import com.example.backend.dto.*;
import com.example.backend.entity.*;
import com.example.backend.entity.embeddableIds.UserLessonId;
import com.example.backend.entity.embeddableIds.UserProblemId;
import com.example.backend.entity.embeddableIds.UserQuestionId;
import com.example.backend.exceptions.AuthenticationException;
import com.example.backend.exceptions.CrudOperationException;
import com.example.backend.repository.*;
import com.example.backend.utils.PasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.*;
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

    private final UserLessonRepository userLessonRepository;

    private final LessonRepository lessonRepository;

    private final PasswordEncoder passwordEncoder;

    private static final long EXPIRATION_TIME = 36000;

    private final SecretKey secretKey = generateSecretKey();


    public UserService(UserRepository userRepository, UserResultsRepository userResultsRepository, QuestionRepository questionRepository, RoleRepository roleRepository, ProblemRepository problemRepository, UserProblemResultsRepository userProblemResultsRepository, CourseRepository courseRepository, UserLessonRepository userLessonRepository, LessonRepository lessonRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userResultsRepository = userResultsRepository;
        this.questionRepository = questionRepository;
        this.roleRepository = roleRepository;
        this.problemRepository = problemRepository;
        this.userProblemResultsRepository = userProblemResultsRepository;
        this.courseRepository = courseRepository;
        this.userLessonRepository = userLessonRepository;
        this.lessonRepository = lessonRepository;
        this.passwordEncoder = passwordEncoder;
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
        user.setUsername(userDto.getUsername());

        userRepository.save(user);
        userDto.setUserId(user.getUserId());
        userDto.setPassword(user.getPassword());

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
                            .build();
                    if (user.getRoles() != null && user.getRoles().size() > 0) {
                        userDto.setUserType(user.getRoles().iterator().next().getRole());
                    }
                    userDtos.add(userDto);
                }
        );
        return userDtos;
    }

    public void changePassword(Long id, ChangePasswordDto changePasswordDto) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("old password: " + changePasswordDto.getOldPassword() + " password from db: " + user.getPassword());
        boolean passwordsMatch = passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword());
        System.out.println("paswword match: " + passwordsMatch);
        if (!passwordsMatch) {
            throw new AuthenticationException("Your old password is not correct!");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
    }

    private SecretKey generateSecretKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA512");
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate secret key", e);
        }
    }
    public String forgotPassword(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new AuthenticationException("This email is not linked to any user!");
        }
        User userByEmail = user.get();

        String token = PasswordGenerator.generateRandomPassword();

        userByEmail.setPassword(passwordEncoder.encode(token));
        userRepository.save(userByEmail);
        return token;
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

        if (user.getUserQuestionResults() == null) {
            user.setUserQuestionResults(new ArrayList<>());
        }

        UserQuestionResults userResult = UserQuestionResults.builder()
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

        UserProblemResults userProblemResult = UserProblemResults.builder()
                .userProblemId(new UserProblemId(userId, problemId))
                .user(user)
                .problem(problem)
                .percentage(scoreAnswerDto.getScore())
                .answer(scoreAnswerDto.getAnswer())
                .build();

        userProblemResultsRepository.save(userProblemResult);

        return UserProblemResultDto.builder()
                .userId(userId)
                .problemId(problemId)
                .problemDescription(problem.getDescription())
                .percentage(userProblemResult.getPercentage())
                .answer(userProblemResult.getAnswer())
                .problemName(userProblemResult.getProblem().getName())
                .problemDifficulty(userProblemResult.getProblem().getDifficulty().toString())
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

    public ScoreAnswerDto getProblemScoreForAProblemSoledByUser(Long userId, Long problemId) {
        Optional<UserProblemResults> problemResultOptional = userProblemResultsRepository.findById(new UserProblemId(userId, problemId));
        if (problemResultOptional.isEmpty())
            return null;
        return ScoreAnswerDto.builder()
                .score(problemResultOptional.get().getPercentage())
                .answer(problemResultOptional.get().getAnswer())
                .build();
    }

    public List<UserResultsDto> getQuestionsResultsForStudent(Long userId) {
        List<UserQuestionResults> userQuestionResults = userResultsRepository.findByUser_UserId(userId);
        return userQuestionResults.stream().map((userResult -> UserResultsDto.builder()
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
                        .problemName(userProblemResult.getProblem().getName())
                        .problemId(userProblemResult.getProblem().getProblemId())
                        .problemDescription(userProblemResult.getProblem().getDescription())
                        .percentage(userProblemResult.getPercentage())
                        .answer(userProblemResult.getAnswer())
                        .problemDifficulty(userProblemResult.getProblem().getDifficulty().toString())
                        .build()))
                .toList();
    }

    public List<CourseDto> findTop8Courses() {
        List<Course> courses = userRepository.findTop8Courses().stream().limit(8).toList();
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
        List<Problem> problems = userRepository.findTop8Problems().stream().limit(8).toList();

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

        // Initialize and load the courses collection
        Hibernate.initialize(user.getCourses());

        if (user.getCourses() == null || user.getCourses().isEmpty()) {
            user.setCourses(new ArrayList<>());
        }

        if (!user.getCourses().contains(course)) {
            user.getCourses().add(course);
        }


        userRepository.save(user);

        return CourseDto.builder()
                .id(course.getCourseId())
                .name(course.getName())
                .description(course.getDescription())
                .difficulty(course.getDifficulty().toString())
                .courseType(course.getCourseTypes().iterator().next().getType())
                .build();
    }

    @Transactional
    public void removeCourseFromStudent(Long userId, Long courseId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });

        courseRepository.findById(courseId).orElseThrow(() -> {
            throw new CrudOperationException("Course does not exist!");
        });
        // Initialize and load the courses collection
        Hibernate.initialize(user.getCourses());

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

    @Transactional
    public boolean checkIfUserEnrolledToCourse(Long userId, Long courseId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });

        // Initialize and load the courses collection
        Hibernate.initialize(user.getCourses());

        List<Course> courses = user.getCourses();

        for (Course course : courses) {
            if (course.getCourseId() == courseId)
                return true;
        }
        return false;
    }

    public LessonDto addEnrolledLessonToUser(Long userId, Long lessonId, Long courseId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });

        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> {
            throw new CrudOperationException("Lesson does not exist!");
        });

        courseRepository.findById(courseId).orElseThrow(() -> {
            throw new CrudOperationException("Course does not exist!");
        });

        UserLessons userLessons = UserLessons.builder()
                .userLessonId(new UserLessonId(userId, lessonId))
                .lesson(lesson)
                .user(user)
                .courseId(courseId)
                .build();

        userLessonRepository.save(userLessons);

        return LessonDto.builder()
                .noLesson(lesson.getNoLesson())
                .id(lessonId)
                .expectedTime(lesson.getExpectedTime())
                .description(lesson.getDescription())
                .longDescription(lesson.getLongDescription())
                .name(lesson.getName())
                .build();
    }

    public boolean checkIfAUserLessonIsComplete(Long userId, Long lessonId, Long courseId) {
        Optional<UserLessons> userLessonsOptional = userLessonRepository.findByUserLessonIdAndCourseId(new UserLessonId(userId, lessonId), courseId);
        if (userLessonsOptional.isEmpty()) {
            return false;
        }

        UserLessons userLessons = userLessonsOptional.get();

        return userLessons.isCompleted();
    }

    public void markLessonAsCompleted(Long userId, Long lessonId, Long courseId) {
        Optional<UserLessons> userLessonsOptional = userLessonRepository.findByUserLessonIdAndCourseId(new UserLessonId(userId, lessonId), courseId);
        if (userLessonsOptional.isEmpty()) {
            throw new CrudOperationException("This user is not enrolled to this course");
        }

        UserLessons userLessons = userLessonsOptional.get();
        userLessons.setCompleted(true);
        userLessonRepository.save(userLessons);
    }

    public boolean checkIfAllLessonsOfACourseAreComplete(Long userId, Long courseId, int length) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });

        int completedLessons = 0;

        List<UserLessons> userLessons = userLessonRepository.findAllByUserAndCourseId(user, courseId);
        for (UserLessons userLesson : userLessons) {
            if (userLesson.isCompleted()) {
                completedLessons++;
            }
        }
        return completedLessons == length;
    }
}
