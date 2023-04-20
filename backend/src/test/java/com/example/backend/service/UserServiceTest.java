package com.example.backend.service;

import com.example.backend.dto.UserDto;
import com.example.backend.entity.Question;
import com.example.backend.entity.Quiz;
import com.example.backend.entity.Role;
import com.example.backend.entity.User;
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

    private Quiz quiz;


    @BeforeEach
    public void setUp() {
        quiz = Quiz.builder()
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

//    @Test
//    public void givenQuestion_findQuestionById_throwExceptionIfQuestionDoesntExist() {
//        // given
//        Question question = Question.builder()
//                .questionId(1L)
//                .quiz(quiz)
//                .score(2)
//                .description("test")
//                .build();
//
//        when(questionRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        // when
//        Executable call = () -> questionService.getQuestionById(question.getQuestionId());
//
//        // then
//        assertThrows(RuntimeException.class, call);
//    }
}
