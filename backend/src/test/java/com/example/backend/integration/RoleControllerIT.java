package com.example.backend.integration;

import com.example.backend.controller.RoleController;
import com.example.backend.dto.RoleDto;
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
public class RoleControllerIT {

    @Autowired
    private RoleController roleController;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DirtiesContext
    void testRoleIsPersistedCorrectlyThroughRoleController() {
        RoleDto roleDto = RoleDto.builder()
                .role("test")
                .build();

        // add role
        RoleDto saved = roleController.addRole(roleDto).getBody();
        assertNotNull(saved);

        // getRoleById
        Long roleId = saved.getId();
        RoleDto queriedRoleDto = roleController.getRoleById(roleId).getBody();
        assertNotNull(queriedRoleDto);
        assertEquals(roleDto.getRole(), queriedRoleDto.getRole());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "role"));

        // get roles
        List<RoleDto> allRoleDtos = roleController.getAllRoles().getBody();
        assertNotNull(allRoleDtos);
        assertEquals(1, allRoleDtos.size());

        // update role
        RoleDto updateRoleDto = RoleDto.builder()
                .role("test update")
                .build();
        RoleDto savedUpdated = roleController.updateRole(saved.getId(), updateRoleDto).getBody();
        assertNotNull(savedUpdated);
        RoleDto updateQueriedRoleDto = roleController.getRoleById(roleId).getBody();
        assert updateQueriedRoleDto != null;
        assertEquals(updateRoleDto.getRole(), updateQueriedRoleDto.getRole());

        // delete role
        roleController.removeRole(saved.getId());
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "role"));
    }

    @Test
    @DirtiesContext
    void testRoleAdd1000RolesThroughRoleController() {
        final int TIMES = 1000;

        for (int i = 0; i < TIMES; ++i) {
            RoleDto roleDto = RoleDto.builder().id((long) i).build();

            // add role
            RoleDto saved = roleController.addRole(roleDto).getBody();
            assertNotNull(saved);
        }
        assertEquals(TIMES, JdbcTestUtils.countRowsInTable(jdbcTemplate, "role"));

        // get roles
        List<RoleDto> allRoleDtos = roleController.getAllRoles().getBody();
        assertNotNull(allRoleDtos);
        assertEquals(TIMES, allRoleDtos.size());
    }

    @Test
    @DirtiesContext
    void testRoleAddRoleWithSetIdThroughRoleController() {
        RoleDto roleDto = RoleDto.builder()
                .role("test")
                .id(10L)
                .build();

        // add role
        RoleDto saved = roleController.addRole(roleDto).getBody();
        assertNotNull(saved);

        RoleDto roleDto1 = RoleDto.builder()
                .role("test1")
                .id(saved.getId())
                .build();

        // add role
        RoleDto saved1 = roleController.addRole(roleDto1).getBody();
        assertNotNull(saved1);

        assertNotEquals(saved.getId(), saved1.getId());
    }
}
