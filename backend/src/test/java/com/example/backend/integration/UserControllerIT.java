package com.example.backend.integration;

import com.example.backend.controller.UserController;
import com.example.backend.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UserControllerIT {
    @Autowired
    private UserController userController;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DirtiesContext
    void testUserIsPersistedCorrectlyThroughUserController() {
        UserDto userDto = UserDto.builder()
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .build();

        // add user
        UserDto saved = userController.addUser(userDto).getBody();
        assertNotNull(saved);

        // getUserById
        Long userId = saved.getUserId();
        UserDto queriedUserDto = userController.getUserById(userId).getBody();
        assertNotNull(queriedUserDto);
        assertEquals(userDto.getFirstName(), queriedUserDto.getFirstName());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));

        // get users
        List<UserDto> allUserDtos = userController.getAllUsers().getBody();
        assertNotNull(allUserDtos);
        assertEquals(1, allUserDtos.size());

        // update user
        UserDto updateUserDto = UserDto.builder()
                .firstName("first name update")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .build();
        UserDto savedUpdated = userController.updateUser(saved.getUserId(), updateUserDto).getBody();
        assertNotNull(savedUpdated);
        UserDto updateQueriedUserDto = userController.getUserById(userId).getBody();
        assert updateQueriedUserDto != null;
        assertEquals(updateUserDto.getFirstName(), updateQueriedUserDto.getFirstName());

        // delete user
        userController.removeUser(saved.getUserId());
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
    }

    @Test
    @DirtiesContext
    void testUserAdd1000UsersThroughUserController() {
        final int TIMES = 1000;

        for (int i = 0; i < TIMES; ++i) {
            UserDto userDto = UserDto.builder()
                    .userId((long) i)
                    .firstName("first name")
                    .lastName("last name")
                    .username("username" + String.valueOf(i))
                    .email("email" + String.valueOf(i))
                    .password("password")
                    .build();

            // add user
            UserDto saved = userController.addUser(userDto).getBody();
            assertNotNull(saved);
        }
        assertEquals(TIMES, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));

        // get users
        List<UserDto> allUserDtos = userController.getAllUsers().getBody();
        assertNotNull(allUserDtos);
        assertEquals(TIMES, allUserDtos.size());
    }

    @Test
    @DirtiesContext
    void testUserAddUserWithSetIdThroughUserController() {
        UserDto userDto = UserDto.builder()
                .firstName("first name")
                .lastName("last name")
                .username("username")
                .email("email")
                .password("password")
                .userId(10L)
                .build();

        // add user
        UserDto saved = userController.addUser(userDto).getBody();
        assertNotNull(saved);

        UserDto userDto1 = UserDto.builder()
                .firstName("first name1")
                .lastName("last name")
                .username("username1")
                .email("email1")
                .password("password")
                .userId(userDto.getUserId())
                .build();

        // add user
        UserDto saved1 = userController.addUser(userDto1).getBody();
        assertNotNull(saved1);

        assertNotEquals(saved.getUserId(), saved1.getUserId());
    }

}
