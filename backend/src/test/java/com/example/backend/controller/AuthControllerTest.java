package com.example.backend.controller;

import com.example.backend.dto.LoginDto;
import com.example.backend.dto.SignUpDto;
import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import com.example.backend.exceptions.AuthenticationException;
import com.example.backend.repository.RoleRepository;
import com.example.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testAuthenticateUserWithValidCredentials() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setUsernameOrEmail("testuser");
        loginDto.setPassword("password");

        User user = User.builder()
                .userId(1L)
                .username("testUser")
                .password(passwordEncoder.encode("password"))
                .build();
//        System.out.println(user.getPassword());
//
//        when(userRepository.findByUsernameOrEmail(loginDto.getUsernameOrEmail(), loginDto.getUsernameOrEmail()))
//                .thenReturn(Optional.of(user));
//        System.out.println(user.getPassword());
//
//        when(authenticationManager.authenticate(ArgumentMatchers.any()))
//                .thenReturn(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
//
//        // Act
//        ResponseEntity<?> responseEntity = authController.authenticateUser(loginDto);
//
//        // Assert
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals("true", responseEntity.getBody());
    }

    @Test
    public void testAuthenticateUserWithInvalidCredentials() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setUsernameOrEmail("testuser");
        loginDto.setPassword("wrongpassword");

        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("password"));

        when(userRepository.findByUsernameOrEmail(loginDto.getUsernameOrEmail(), loginDto.getUsernameOrEmail()))
                .thenReturn(Optional.of(user));

        // Act and Assert
        assertThrows(AuthenticationException.class, () -> {
            authController.authenticateUser(loginDto);
        });
    }

    @Test
    public void testRegisterUserWithValidData() {
        // Arrange
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setFirstName("John");
        signUpDto.setLastName("Doe");
        signUpDto.setUsername("johndoe");
        signUpDto.setEmail("johndoe@example.com");
        signUpDto.setPassword("password");

        when(userRepository.existsByUsername(signUpDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signUpDto.getPassword())).thenReturn("encodedpassword");

        Role role = new Role();
        role.setRole("student");
        when(roleRepository.findByRole("student")).thenReturn(Optional.of(role));

        User savedUser = new User();
        savedUser.setUserId(1L);
        savedUser.setFirstName(signUpDto.getFirstName());
        savedUser.setLastName(signUpDto.getLastName());
        savedUser.setUsername(signUpDto.getUsername());
        savedUser.setEmail(signUpDto.getEmail());
        savedUser.setPassword("encodedpassword");
        savedUser.setRoles(Collections.singleton(role));
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(savedUser);

        // Act
        ResponseEntity<?> responseEntity = authController.registerUser(signUpDto);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("true", responseEntity.getBody());
    }

    @Test
    public void testRegisterUserWithExistingUsername() {
        // Arrange
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setUsername("existingusername");

        when(userRepository.existsByUsername(signUpDto.getUsername())).thenReturn(true);

        // Act
        ResponseEntity<?> responseEntity = authController.registerUser(signUpDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Username is already taken!", responseEntity.getBody());
    }
}
