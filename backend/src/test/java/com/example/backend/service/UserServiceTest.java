package com.example.backend.service;

import com.example.backend.dto.UserAndFinalScoreDto;
import com.example.backend.dto.UserDto;
import com.example.backend.dto.UserResultsDto;
import com.example.backend.dto.UserTestResultDto;
import com.example.backend.entity.*;
import com.example.backend.repository.QuestionRepository;
import com.example.backend.repository.RoleRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.UserResultsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @InjectMocks
    private UserService userService;

    private Role role;

    private Question question;


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
        UserResultsDto expected = userService.addQuestionAnswerAndQuestionScoreToStudent(user.getUserId(), question.getQuestionId(), score, answer);

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
        Executable call = () -> userService.addQuestionAnswerAndQuestionScoreToStudent(user.getUserId(), question.getQuestionId(), score, answer);

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
        Executable call = () -> userService.addQuestionAnswerAndQuestionScoreToStudent(user.getUserId(), question.getQuestionId(), score, answer);

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

        UserResults userResult = UserResults.builder()
                .userQuestionId(new UserQuestionId(user.getUserId(), question.getQuestionId()))
                .user(user)
                .question(question)
                .score(score)
                .answer(answer)
                .build();
        List<UserResults> userResults = new ArrayList<>();
        userResults.add(userResult);

        when(userResultsRepository.findByUser_UserId(user.getUserId())).thenReturn(userResults);

        // when
        List<UserResultsDto> expected = userService.getQuestionsResultsForStudent(user.getUserId());
        List<UserResultsDto> actual = userResults.stream().map(userResults1 ->
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
    public void givenUser_computeTestScoreForUser_returnUserTestResultDto() {
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

        UserResults userResult = UserResults.builder()
                .userQuestionId(new UserQuestionId(user.getUserId(), question.getQuestionId()))
                .user(user)
                .question(question)
                .score(3)
                .answer("answer")
                .build();

        List<UserResults> userResults = new ArrayList<>();
        userResults.add(userResult);
        userResults.add(userResult);

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(userResultsRepository.findByUser_UserId(user.getUserId())).thenReturn(userResults);

        // when
        UserTestResultDto expected = userService.computeTestScoreForUser(user.getUserId());
        UserTestResultDto actual = UserTestResultDto.builder()
                .userId(user.getUserId())
                .userFirstName(user.getFirstName())
                .userLastName(user.getLastName())
                .testResult(6)
                .build();

        // then
        assertEquals(expected, actual);
        verify(userResultsRepository).findByUser_UserId(user.getUserId());
    }

    @Test
    public void givenUser_computeTestScoreForUser_throwExceptionIfUserDoesntExist() {
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

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> userService.computeTestScoreForUser(user.getUserId());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenAllUsers_getAllUsersFinalScores_returnAllUsersWithTestResults() {
        // given
        User user = User.builder()
                .firstName("first name")
                .lastName("last name")
                .roles(new HashSet<>(Collections.singletonList(role)))
                .username("username")
                .email("email")
                .password("password")
                .testResult(7)
                .build();

        List<User> users = new ArrayList<>();
        users.add(user);

        when(userRepository.findAll()).thenReturn(users);

        // when
        List<UserAndFinalScoreDto> expected = userService.getAllUsersFinalScores();
        List<UserAndFinalScoreDto> actual = users.stream().map(user1 ->
                UserAndFinalScoreDto.builder()
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .userId(user.getUserId())
                        .testResult(user.getTestResult())
                        .build()).toList();

        // then
        assertEquals(expected, actual);
        verify(userRepository).findAll();
    }
}
