package com.example.backend.service;

import com.example.backend.dto.*;
import com.example.backend.entity.*;
import com.example.backend.entity.embeddableIds.UserLessonId;
import com.example.backend.entity.embeddableIds.UserProblemId;
import com.example.backend.entity.embeddableIds.UserQuestionId;
import com.example.backend.entity.enums.Difficulty;
import com.example.backend.entity.enums.ReturnType;
import com.example.backend.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserResultsRepository userResultsRepository;

    @Mock
    private UserProblemResultsRepository userProblemResultsRepository;

    @Mock
    private ProblemRepository problemRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private UserLessonRepository userLessonRepository;

    @InjectMocks
    private UserService userService;

    private Role role;

    private Question question;

    private Problem problem;


    @BeforeEach
    public void setUp() {
        Quiz quiz = Quiz.builder()
                .quizId(1L)
                .name("test quiz")
                .noQuestions(10)
                .build();

        question = Question.builder()
                .questionId(10L)
                .quiz(quiz)
                .score(2)
                .description("test")
                .build();

        role = Role.builder()
                .roleId(100L)
                .role("test")
                .build();

        problem = Problem.builder()
                .problemId(2L)
                .name("test")
                .returnType(ReturnType.RETURN)
                .difficulty(Difficulty.BEGINNER)
                .build();
    }

    @Test
    public void givenUserDto_addUser_returnEqualUserDto() {
        // given
        UserDto userDto = UserDto.builder()
                .firstName("first name")
                .lastName("last name")
                .userType(role.getRole())
                .username("username")
                .email("email")
                .password("password")
                .build();

        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .roles(new HashSet<>(Collections.singletonList(role)))
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .build();

        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);


        // when
        UserDto newUserDto = userService.addUser(userDto);

        // then
        assertEquals(newUserDto, userDto);
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void givenUserDto_addUser_throwsExceptionIfEmailAlreadyTaken() {
        // given
        UserDto userDto = UserDto.builder()
                .firstName("first name")
                .lastName("last name")
                .userType(role.getRole())
                .username("username")
                .email("email")
                .password("password")
                .build();

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // when
        Executable call = () -> userService.addUser(userDto);

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenUserDto_addUser_throwsExceptionIfUsernameAlreadyTaken() {
        // given
        UserDto userDto = UserDto.builder()
                .firstName("first name")
                .lastName("last name")
                .userType(role.getRole())
                .username("username")
                .email("email")
                .password("password")
                .build();

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(true);


        // when
        Executable call = () -> userService.addUser(userDto);

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenAllUsers_getUsers_returnAllUsers() {
        // given
        User user = User.builder()
                .firstName("first name")
                .lastName("last name")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .username("username")
                .email("email")
                .password("password")
                .build();

        List<User> users = new ArrayList<>();
        users.add(user);

        when(userRepository.findAll()).thenReturn(users);

        // when
        List<UserDto> expected = userService.getAllUsers();
        List<UserDto> actual = users.stream().map(user1 ->
                UserDto.builder()
                        .firstName(user1.getFirstName())
                        .lastName(user1.getLastName())
                        .userType(user1.getRoles().iterator().next().getRole())
                        .username(user1.getUsername())
                        .email(user1.getEmail())
                        .password(user1.getPassword())
                        .build()).toList();

        // then
        assertEquals(expected, actual);
        verify(userRepository).findAll();
    }

    @Test
    public void givenUser_removeUser_deleteUserIfFound() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .username("username")
                .email("email")
                .password("password")
                .build();

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        // when
        userService.removeUser(user.getUserId());

        // then
        verify(userRepository).findById(user.getUserId());
    }

    @Test
    public void givenUser_removeUser_throwExceptionIfUserExists() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .username("username")
                .email("email")
                .password("password")
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> userService.removeUser(user.getUserId());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenUserAndUserDto_updateUser_updateUserIfFound() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .username("username")
                .email("email")
                .password("password")
                .build();

        UserDto userDto = UserDto.builder()
                .firstName("first name update")
                .lastName("last name")
                .userType(role.getRole())
                .username("username")
                .email("email")
                .password("password")
                .build();

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        // when
        userService.updateUser(user.getUserId(), userDto);

        // then
        assertEquals(user.getFirstName(), userDto.getFirstName());
        verify(userRepository).findById(user.getUserId());
    }

    @Test
    public void givenUserAndUserDto_updateUser_throwExceptionIfUserExists() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .username("username")
                .email("email")
                .password("password")
                .build();

        UserDto userDto = UserDto.builder()
                .firstName("first name update")
                .lastName("last name")
                .userType(role.getRole())
                .username("username")
                .email("email")
                .password("password")
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> userService.updateUser(user.getUserId(), userDto);

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenUser_findUserById_returnEqualUserDtoIfFound() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        // when
        UserDto expected = userService.getUserById(user.getUserId());
        UserDto actual = UserDto.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .userType(user.getRoles().iterator().next().getRole())
                .build();

        // then
        assertEquals(expected, actual);
        verify(userRepository).findById(user.getUserId());
    }

    @Test
    public void givenUser_findUserById_throwExceptionIfUserExists() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> userService.getUserById(user.getUserId());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenUser_findUserByEmail_returnEqualUserDtoIfFound() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // when
        UserDto expected = userService.findUserByEmail(user.getEmail());
        UserDto actual = UserDto.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .userType(user.getRoles().iterator().next().getRole())
                .build();

        // then
        assertEquals(expected, actual);
        verify(userRepository).findByEmail(user.getEmail());
    }

    @Test
    public void givenUser_findUserByEmail_throwExceptionIfUserExists() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // when
        Executable call = () -> userService.findUserByEmail(user.getEmail());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenUser_findUserByUsername_returnEqualUserDtoIfFound() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // when
        UserDto expected = userService.findUserByUsername(user.getUsername());
        UserDto actual = UserDto.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .userType(user.getRoles().iterator().next().getRole())
                .build();

        // then
        assertEquals(expected, actual);
        verify(userRepository).findByUsername(user.getUsername());
    }

    @Test
    public void givenUser_findUserByUsername_throwExceptionIfUserExists() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .build();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // when
        Executable call = () -> userService.findUserByUsername(user.getEmail());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenUser_findUserByUsernameOrEmail_returnEqualUserDtoIfFound() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        when(userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail())).thenReturn(Optional.of(user));

        // when
        UserDto expected = userService.findUserByUsernameOrEmail(user.getUsername(), user.getEmail());
        UserDto actual = UserDto.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .userType(user.getRoles().iterator().next().getRole())
                .build();

        // then
        assertEquals(expected, actual);
        verify(userRepository).findByUsernameOrEmail(user.getUsername(), user.getEmail());
    }

    @Test
    public void givenUser_findUserByUsernameOrEmail_throwExceptionIfUserExists() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .build();

        when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.empty());

        // when
        Executable call = () -> userService.findUserByUsernameOrEmail(user.getEmail(), user.getEmail());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenUserAndRole_assignRoleToUser_returnUserHavingARole() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .build();

        when(roleRepository.findById(role.getRoleId())).thenReturn(Optional.of(role));
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        // when
        userService.assignRoleToUser(user.getUserId(), role.getRoleId());

        // then
        assertEquals(user.getRoles().stream().iterator().next(), role);
        verify(userRepository).findById(user.getUserId());
        verify(roleRepository).findById(role.getRoleId());
    }

    @Test
    public void givenUserAndRole_assignRoleToUser_throwExceptionIfUserDoesntExist() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .build();

        when(roleRepository.findById(role.getRoleId())).thenReturn(Optional.of(role));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> userService.assignRoleToUser(user.getUserId(), role.getRoleId());

        // then
        assertThrows(RuntimeException.class, call);
        verify(roleRepository).findById(role.getRoleId());
    }

    @Test
    public void givenUserAndRole_assignRoleToUser_throwExceptionIfRoleDoesntExist() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .build();

        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> userService.assignRoleToUser(user.getUserId(), role.getRoleId());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenUserAndRole_assignRoleToUser_throwExceptionIfUserAlreadyHasARole() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        when(roleRepository.findById(role.getRoleId())).thenReturn(Optional.of(role));
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        // when
        Executable call = () -> userService.assignRoleToUser(user.getUserId(), role.getRoleId());

        // then
        assertThrows(RuntimeException.class, call);
        verify(roleRepository).findById(role.getRoleId());
        verify(userRepository).findById(user.getUserId());
    }

    @Test
    public void givenUserAndQuestionAndAnswerAndScore_addAnswerAndQuestionScoreToStudent_addToUserResults() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        String answer = "answer";
        double score = 3;

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(questionRepository.findById(question.getQuestionId())).thenReturn(Optional.of(question));

        // when
        UserResultsDto expected = userService.addQuestionAnswerAndQuestionScoreToStudent(user.getUserId(), question.getQuestionId(), new ScoreAnswerDto(score, answer));
        UserResultsDto actual = UserResultsDto.builder()
                .userId(user.getUserId())
                .userFirstName(user.getFirstName())
                .userLastName(user.getLastName())
                .questionId(question.getQuestionId())
                .questionDescription(question.getDescription())
                .score(score)
                .answer(answer)
                .build();

        // then
        assertEquals(expected, actual);
        verify(userRepository).findById(user.getUserId());
        verify(questionRepository).findById(question.getQuestionId());
    }

    @Test
    public void givenUserAndQuestionAndAnswerAndScore_addAnswerAndQuestionScoreToStudent_throwExceptionIfUserDoesntExist() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        String answer = "answer";
        double score = 3;

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> userService.addQuestionAnswerAndQuestionScoreToStudent(user.getUserId(), question.getQuestionId(), new ScoreAnswerDto(score, answer));

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenUserAndQuestionAndAnswerAndScore_addAnswerAndQuestionScoreToStudent_throwExceptionIfQuestionDoesntExist() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        String answer = "answer";
        double score = 3;

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(questionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> userService.addQuestionAnswerAndQuestionScoreToStudent(user.getUserId(), question.getQuestionId(), new ScoreAnswerDto(score, answer));

        // then
        assertThrows(RuntimeException.class, call);
        verify(userRepository).findById(user.getUserId());
    }

    @Test
    public void givenUserAndProblemAndAnswerAndScore_addAnswerAndProblemScoreToStudent_addToUserResults() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        String answer = "answer";
        double score = 3;

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(problemRepository.findById(problem.getProblemId())).thenReturn(Optional.of(problem));

        // when
        UserProblemResultDto expected = userService.addAnswerAndProblemPercentageToStudent(user.getUserId(), problem.getProblemId(), new ScoreAnswerDto(score, answer));
        UserProblemResultDto actual = UserProblemResultDto.builder()
                .userId(user.getUserId())
                .problemName(problem.getName())
                .problemId(problem.getProblemId())
                .problemDifficulty(problem.getDifficulty().toString())
                .percentage(score)
                .answer(answer)
                .build();

        // then
        assertEquals(expected, actual);
        verify(userRepository).findById(user.getUserId());
        verify(problemRepository).findById(problem.getProblemId());
    }

    @Test
    public void givenUserAndProblemAndAnswerAndScore_addAnswerAndProblemScoreToStudent_throwExceptionIfUserDoesntExist() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        String answer = "answer";
        double score = 3;

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> userService.addAnswerAndProblemPercentageToStudent(user.getUserId(), problem.getProblemId(), new ScoreAnswerDto(score, answer));

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenUserAndProblemAndAnswerAndScore_addAnswerAndProblemScoreToStudent_throwExceptionIfProblemDoesntExist() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        String answer = "answer";
        double score = 3;

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(problemRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> userService.addAnswerAndProblemPercentageToStudent(user.getUserId(), problem.getProblemId(), new ScoreAnswerDto(score, answer));

        // then
        assertThrows(RuntimeException.class, call);
        verify(userRepository).findById(user.getUserId());
    }

    @Test
    public void givenUser_getResultsForStudent_returnAllResultsForUser() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        String answer = "answer";
        double score = 3;

        UserQuestionResults userResult = UserQuestionResults.builder()
                .userQuestionId(new UserQuestionId(user.getUserId(), question.getQuestionId()))
                .user(user)
                .question(question)
                .score(score)
                .answer(answer)
                .build();
        List<UserQuestionResults> userQuestionResults = new ArrayList<>();
        userQuestionResults.add(userResult);

        when(userResultsRepository.findByUser_UserId(user.getUserId())).thenReturn(userQuestionResults);

        // when
        List<UserResultsDto> expected = userService.getQuestionsResultsForStudent(user.getUserId());
        List<UserResultsDto> actual = userQuestionResults.stream().map(userResults1 ->
                UserResultsDto.builder()
                        .userId(userResults1.getUser().getUserId())
                        .userFirstName(userResults1.getUser().getFirstName())
                        .userLastName(userResults1.getUser().getLastName())
                        .questionId(userResults1.getQuestion().getQuestionId())
                        .questionDescription(userResults1.getQuestion().getDescription())
                        .score(userResults1.getScore())
                        .answer(userResults1.getAnswer())
                        .build()).toList();

        // then
        assertEquals(expected, actual);
        verify(userResultsRepository).findByUser_UserId(user.getUserId());
    }

    @Test
    public void givenUser_getProblemResultsForStudent_returnAllResultsForUser() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        String answer = "answer";
        double score = 3;

        UserProblemResults userResult = UserProblemResults.builder()
                .userProblemId(new UserProblemId(user.getUserId(), problem.getProblemId()))
                .user(user)
                .problem(problem)
                .percentage(score)
                .answer(answer)
                .build();
        List<UserProblemResults> userProblemResults = new ArrayList<>();
        userProblemResults.add(userResult);

        when(userProblemResultsRepository.findByUser_UserId(user.getUserId())).thenReturn(userProblemResults);

        // when
        List<UserProblemResultDto> expected = userService.getProblemsResultsForStudent(user.getUserId());
        List<UserProblemResultDto> actual = userProblemResults.stream().map(userResults1 ->
                UserProblemResultDto.builder()
                        .userId(userResults1.getUser().getUserId())
                        .problemId(userResults1.getProblem().getProblemId())
                        .problemName(userResults1.getProblem().getName())
                        .problemDifficulty(userResults1.getProblem().getDifficulty().toString())
                        .percentage(userResults1.getPercentage())
                        .answer(userResults1.getAnswer())
                        .build()).toList();

        // then
        assertEquals(expected, actual);
        verify(userProblemResultsRepository).findByUser_UserId(user.getUserId());
    }

    @Test
    public void givenUserAndCourses_findTop8Courses_returnCourseDtos() {
        // given
        Course course1 = Course.builder()
                .courseId(1L)
                .name("course name")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        Course course2 = Course.builder()
                .courseId(2L)
                .name("course name")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        List<Course> courses = new ArrayList<>();
        courses.add(course1);
        courses.add(course2);

        when(userRepository.findTop8Courses()).thenReturn(courses);

        // Invoke the method
        List<CourseDto> dtos = userService.findTop8Courses();

        // Assertions
        assertEquals(2, dtos.size());
        verify(userRepository).findTop8Courses();
    }

    @Test
    public void givenUserAndProblems_findTop8Courses_returnProblemDtos() {
        // given
        Problem problem1 = Problem.builder()
                .problemId(1L)
                .name("test")
                .returnType(ReturnType.RETURN)
                .difficulty(Difficulty.BEGINNER)
                .build();

        Problem problem2 = Problem.builder()
                .problemId(2L)
                .name("test")
                .returnType(ReturnType.RETURN)
                .difficulty(Difficulty.BEGINNER)
                .build();

        List<Problem> problems = new ArrayList<>();
        problems.add(problem1);
        problems.add(problem2);

        when(userRepository.findTop8Problems()).thenReturn(problems);

        // Invoke the method
        List<ProblemDto> dtos = userService.findTop8Problems();

        // Assertions
        assertEquals(2, dtos.size());
        verify(userRepository).findTop8Problems();
    }

    @Test
    public void givenCourseAndUser_addLCourseToUser_addLCourseToUser() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        Course course = Course.builder()
                .courseId(1L)
                .name("course name")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        when(courseRepository.findById(course.getCourseId())).thenReturn(Optional.of(course));
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        // when
        CourseDto expected = userService.addCourseToStudent(user.getUserId(), course.getCourseId());
        CourseDto actual = CourseDto.builder()
                .id(course.getCourseId())
                .name(course.getName())
                .difficulty(course.getDifficulty().toString())
                .courseType(course.getCourseTypes().iterator().next().getType())
                .build();

        // then
        assertEquals(expected, actual);
        verify(courseRepository).findById(course.getCourseId());
        verify(userRepository).findById(user.getUserId());
    }

    @Test
    public void givenLessonAndNotOkCourse_addLessonToCourse_throwExceptionIfCourseDoesntExist() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        Course course = Course.builder()
                .courseId(1L)
                .name("course name")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> userService.addCourseToStudent(user.getUserId(), course.getCourseId());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenNotOkLessonAndCourse_addLessonToCourse_throwExceptionIfLessonDoesntExist() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        Course course = Course.builder()
                .courseId(1L)
                .name("course name")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> userService.addCourseToStudent(user.getUserId(), course.getCourseId());

        // then
        assertThrows(RuntimeException.class, call);
        verify(courseRepository).findById(course.getCourseId());
    }

    @Test
    public void givenUserAndCourse_removeCourseFromUser_removeCourseFromUser() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        Course course = Course.builder()
                .courseId(1L)
                .name("course name")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        List<Course> courses = new ArrayList<>();
        courses.add(course);
        user.setCourses(courses);

        when(courseRepository.findById(course.getCourseId())).thenReturn(Optional.of(course));
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        // Act
        userService.removeCourseFromStudent(user.getUserId(), course.getCourseId());

        // Assert
        assertTrue(user.getCourses().isEmpty());
        assertNull(course.getUsers());
        verify(userRepository, times(1)).save(user);
        verify(courseRepository).findById(course.getCourseId());
    }

    @Test
    public void givenCourseAndNotOkUser_removeCourseFromUser_throwExceptionIfUserDoesntExist() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        Course course = Course.builder()
                .courseId(1L)
                .name("course name")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> userService.removeCourseFromStudent(user.getUserId(), course.getCourseId());


        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenNotOkCourseAndUser_removeCourseFromUser_throwExceptionIfCourseDoesntExist() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        Course course = Course.builder()
                .courseId(1L)
                .name("course name")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> userService.removeCourseFromStudent(user.getUserId(), course.getCourseId());

        // then
        assertThrows(RuntimeException.class, call);
        verify(userRepository).findById(user.getUserId());
    }

    @Test
    public void givenUserWithCourses_testGetAllUserCourses_returnListOfCoursesIfFound() {
        //given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        Course course1 = Course.builder()
                .courseId(1L)
                .name("course name1")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        Course course2 = Course.builder()
                .courseId(2L)
                .name("course name2")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        List<Course> courses = new ArrayList<>();
        courses.add(course1);
        courses.add(course2);

        user.setCourses(courses);

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        // when
        List<CourseDto> lessonDtos = userService.getUserCourses(user.getUserId());

        // assert
        assertEquals(2, lessonDtos.size());
        verify(userRepository).findById(user.getUserId());
    }

    @Test
    public void givenNotOkUserWithCourses_testGetAllUserCourses_returnListOfCoursesIfFound() {
        //given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        Course course1 = Course.builder()
                .courseId(1L)
                .name("course name1")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        Course course2 = Course.builder()
                .courseId(2L)
                .name("course name2")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        List<Course> courses = new ArrayList<>();
        courses.add(course1);
        courses.add(course2);

        user.setCourses(courses);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> userService.getUserCourses(user.getUserId());

        // assert
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenUserAndCourse_checkIfUserEnrolledToCourse_UserEnrolled() {
        //given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        Course course1 = Course.builder()
                .courseId(1L)
                .name("course name1")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        Course course2 = Course.builder()
                .courseId(2L)
                .name("course name2")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        List<Course> courses = new ArrayList<>();
        courses.add(course1);
        courses.add(course2);

        user.setCourses(courses);

        // Mock repository behavior
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        // Invoke the method
        boolean result = userService.checkIfUserEnrolledToCourse(user.getUserId(), course1.getCourseId());

        // Assertion
        Assertions.assertTrue(result);
    }

    @Test
    public void givenUserAndCourse_checkIfUserEnrolledToCourse_userNotEnrolled() {
        //given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        Course course1 = Course.builder()
                .courseId(1L)
                .name("course name1")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        Course course2 = Course.builder()
                .courseId(2L)
                .name("course name2")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        List<Course> courses = new ArrayList<>();
        courses.add(course1);

        user.setCourses(courses);

        // Mock repository behavior
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        // Invoke the method
        boolean result = userService.checkIfUserEnrolledToCourse(user.getUserId(), course2.getCourseId());

        // Assertion
        Assertions.assertFalse(result);
    }

    @Test
    public void givenNotOkUserAndCourse_checkIfUserEnrolledToCourse_userNotFound() {
        //given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        Course course1 = Course.builder()
                .courseId(1L)
                .name("course name1")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> userService.checkIfUserEnrolledToCourse(user.getUserId(), course1.getCourseId());

        // assert
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenUserLessonAndCourse_addEnrolledLessonToUser_lessonAndCourseExist() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        Course course = Course.builder()
                .courseId(1L)
                .name("course name1")
                .difficulty(Difficulty.BEGINNER)
                .courseTypes(new HashSet<>(Collections.singleton(CourseType.builder().type("course type").build())))
                .build();

        Lesson lesson = Lesson.builder()
                .lessonId(2L)
                .name("lesson")
                .build();

        // Mock repository behavior
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(lessonRepository.findById(lesson.getLessonId())).thenReturn(Optional.of(lesson));
        when(courseRepository.findById(course.getCourseId())).thenReturn(Optional.of(course));

        // Invoke the method
        LessonDto expected = userService.addEnrolledLessonToUser(user.getUserId(), lesson.getLessonId(), course.getCourseId());
        LessonDto actual = LessonDto.builder()
                .id(lesson.getLessonId())
                .name(lesson.getName())
                .build();

        // Assertion
        assertEquals(expected, actual);
        verify(userRepository).findById(user.getUserId());
        verify(courseRepository).findById(course.getCourseId());
        verify(lessonRepository).findById(lesson.getLessonId());
    }

    @Test
    public void givenNotOkUserLessonAndCourse_addEnrolledLessonToUser_userNotFound() {
        // Mock data
        Long userId = 1L;
        Long lessonId = 1L;
        Long courseId = 1L;

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> userService.addEnrolledLessonToUser(userId, lessonId, courseId);

        // assert
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenUserNotOkLessonAndCourse_addEnrolledLessonToUser_lessonNotFound() {
        // given
        Long userId = 1L;
        Long lessonId = 1L;
        Long courseId = 1L;

        User user = new User();
        user.setUserId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(lessonRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> userService.addEnrolledLessonToUser(userId, lessonId, courseId);

        // assert
        assertThrows(RuntimeException.class, call);
        verify(userRepository).findById(userId);
    }

    @Test
    public void givenUserLessonAndNotOkCourse_addEnrolledLessonToUser_courseNotFound() {
        // given
        Long userId = 1L;
        Long lessonId = 1L;
        Long courseId = 1L;

        User user = new User();
        user.setUserId(userId);

        Lesson lesson = new Lesson();
        lesson.setLessonId(lessonId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> userService.addEnrolledLessonToUser(userId, lessonId, courseId);

        // assert
        assertThrows(RuntimeException.class, call);
        verify(userRepository).findById(userId);
        verify(lessonRepository).findById(lessonId);
    }

    @Test
    public void givenUserLessonAndCourse_checkIfAUserLessonIsComplete_lessonComplete() {
        // Mock data
        Long userId = 1L;
        Long lessonId = 1L;
        Long courseId = 1L;

        UserLessons userLessons = UserLessons.builder()
                .userLessonId(new UserLessonId(userId, lessonId))
                .courseId(courseId)
                .completed(true)
                .build();


        when(userLessonRepository.findByUserLessonIdAndCourseId(
                any(UserLessonId.class),
                any(Long.class)
        ))
                .thenReturn(Optional.of(userLessons));

        // when
        boolean result = userService.checkIfAUserLessonIsComplete(userId, lessonId, courseId);

        // then
        Assertions.assertTrue(result);
    }

    @Test
    public void givenUserLessonAndCourse_checkIfAUserLessonIsComplete_lessonNotComplete() {
        // when
        Long userId = 1L;
        Long lessonId = 1L;
        Long courseId = 1L;

        UserLessons userLessons = new UserLessons();
        userLessons.setCompleted(false);

        when(userLessonRepository.findByUserLessonIdAndCourseId(
                any(UserLessonId.class),
                any(Long.class)
        ))
                .thenReturn(Optional.of(userLessons));

        // when
        boolean result = userService.checkIfAUserLessonIsComplete(userId, lessonId, courseId);

        // then
        Assertions.assertFalse(result);
    }

    @Test
    public void givenUserLessonAndCourse_checkIfAUserLessonIsComplete_userLessonNotFound() {
        // given
        Long userId = 1L;
        Long lessonId = 1L;
        Long courseId = 1L;

        when(userLessonRepository.findByUserLessonIdAndCourseId(
                any(UserLessonId.class),
                any(Long.class)
        ))
                .thenReturn(Optional.empty());

        // when
        boolean result = userService.checkIfAUserLessonIsComplete(userId, lessonId, courseId);

        // then
        Assertions.assertFalse(result);
    }

    @Test
    void givenUserLessonAndCourse_markLessonAsCompleted_lessonMarked() {
        // given
        Long userId = 1L;
        Long lessonId = 2L;
        Long courseId = 3L;

        UserLessons userLessons = new UserLessons();
        userLessons.setCompleted(false);
        Optional<UserLessons> userLessonsOptional = Optional.of(userLessons);

        when(userLessonRepository.findByUserLessonIdAndCourseId(any(UserLessonId.class), any(Long.class)))
                .thenReturn(userLessonsOptional);

        // when
        userService.markLessonAsCompleted(userId, lessonId, courseId);

        // then
        assertTrue(userLessons.isCompleted());
        verify(userLessonRepository, times(1)).save(userLessons);
    }

    @Test
    void givenUserLessonAndCourse_markLessonAsCompleted_NotEnrolledUser_throwsException() {
        // given
        Long userId = 1L;
        Long lessonId = 2L;
        Long courseId = 3L;

        Optional<UserLessons> userLessonsOptional = Optional.empty();

        when(userLessonRepository.findByUserLessonIdAndCourseId(any(UserLessonId.class), any(Long.class)))
                .thenReturn(userLessonsOptional);

        // when
        Executable call = () -> userService.markLessonAsCompleted(userId, lessonId, courseId);

        // then
        assertThrows(RuntimeException.class, call);
        verify(userLessonRepository, never()).save(any(UserLessons.class));
    }

    @Test
    void givenUserLessonAndCourse_checkIfAllLessonsOfACourseAreComplete_allLessonsComplete_returnsTrue() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        Long courseId = 2L;
        int length = 3;

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        List<UserLessons> userLessons = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            UserLessons lesson = new UserLessons();
            lesson.setCompleted(true);
            userLessons.add(lesson);
        }
        when(userLessonRepository.findAllByUserAndCourseId(any(User.class), any(Long.class)))
                .thenReturn(userLessons);

        // when
        boolean result = userService.checkIfAllLessonsOfACourseAreComplete(user.getUserId(), courseId, length);

        // then
        assertTrue(result);
    }

    @Test
    void givenUserLessonAndCourse_checkIfAllLessonsOfACourseAreComplete_notAllLessonsComplete_returnsFalse() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        Long courseId = 2L;
        int length = 3;

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        List<UserLessons> userLessons = new ArrayList<>();
        for (int i = 0; i < length - 1; i++) {
            UserLessons lesson = new UserLessons();
            lesson.setCompleted(true);
            userLessons.add(lesson);
        }

        userLessons.add(new UserLessons());

        when(userLessonRepository.findAllByUserAndCourseId(any(User.class), any(Long.class)))
                .thenReturn(userLessons);

        // Act
        boolean result = userService.checkIfAllLessonsOfACourseAreComplete(user.getUserId(), courseId, length);

        // Assert
        assertFalse(result);
    }

    @Test
    void givenUserLessonAndCourse_checkIfAllLessonsOfACourseAreComplete_userNotFound_throwsException() {
        // given
        User user = User.builder()
                .userId(11L)
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .build();

        Long courseId = 2L;
        int length = 3;

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> userService.checkIfAllLessonsOfACourseAreComplete(user.getUserId(), courseId, length);

        // then
        assertThrows(RuntimeException.class, call);
    }
}
