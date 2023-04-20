package com.example.backend.service;

import com.example.backend.dto.RoleDto;
import com.example.backend.entity.Role;
import com.example.backend.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    public void givenRoleDto_addRole_returnEqualRoleDto() {
        // given
        RoleDto roleDto = RoleDto.builder()
                .role("test")
                .build();

        Role role = Role.builder()
                .role(roleDto.getRole())
                .build();

        when(roleRepository.save(ArgumentMatchers.any(Role.class))).thenReturn(role);

        // when
        RoleDto created = roleService.addRole(roleDto);

        // then
        assertEquals(roleDto, created);
        verify(roleRepository).save(ArgumentMatchers.any(Role.class));
    }

    @Test
    public void givenAllRoles_getRoles_returnAllRoles() {
        // given
        List<Role> roles = new ArrayList<>();
        roles.add(Role.builder().role("test").build());

        when(roleRepository.findAll()).thenReturn(roles);

        // when
        List<RoleDto> expected = roleService.getAllRoles();
        List<RoleDto> actual = roles.stream().map(role ->
                RoleDto.builder().role(role.getRole()).id(role.getRoleId()).build()).toList();

        // then
        assertEquals(expected, actual);
        verify(roleRepository).findAll();
    }

    @Test
    public void givenRole_removeRole_deleteRoleIfFound() {
        // given
        Role role = Role.builder()
                .roleId(1L)
                .role("test")
                .build();

        when(roleRepository.findById(role.getRoleId())).thenReturn(Optional.of(role));

        // when
        roleService.removeRole(role.getRoleId());

        // then
        verify(roleRepository).findById(role.getRoleId());
    }

    @Test
    public void givenRole_removeRole_throwExceptionIfRoleDoesntExist() {
        // given
        Role role = Role.builder()
                .roleId(1L)
                .role("test")
                .build();

        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> roleService.removeRole(role.getRoleId());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenRoleAndRoleDto_updateRole_updateRoleIfFound() {
        // given
        Role role = Role.builder()
                .roleId(1L)
                .role("test")
                .build();

        RoleDto roleDto = RoleDto.builder()
                .role("test role")
                .build();

        when(roleRepository.findById(role.getRoleId())).thenReturn(Optional.of(role));

        // when
        roleService.updateRole(role.getRoleId(), roleDto);

        // then
        assertEquals(role.getRole(), roleDto.getRole());
        verify(roleRepository).findById(role.getRoleId());
    }

    @Test
    public void givenRoleAndRoleDto_updateRole_throwExceptionIfRoleDoesntExist() {
        // given
        Role role = Role.builder()
                .roleId(1L)
                .role("test")
                .build();

        RoleDto roleDto = RoleDto.builder()
                .role("test role")
                .build();

        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> roleService.updateRole(role.getRoleId(), roleDto);

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenRole_findRoleById_returnEqualRoleDtoFound() {
        // given
        Role role = Role.builder()
                .roleId(1L)
                .role("test")
                .build();

        when(roleRepository.findById(role.getRoleId())).thenReturn(Optional.of(role));

        // when
        RoleDto expected = roleService.getRoleById(role.getRoleId());
        RoleDto actual = RoleDto.builder().role(role.getRole()).id(role.getRoleId()).build();

        // then
        assertEquals(expected, actual);
        verify(roleRepository).findById(role.getRoleId());
    }

    @Test
    public void givenRole_findRoleById_throwExceptionIfRoleDoesntExist() {
        // given
        Role role = Role.builder()
                .roleId(1L)
                .role("test")
                .build();

        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> roleService.getRoleById(role.getRoleId());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenRole_findRoleByRole_returnEqualRoleDtoFound() {
        // given
        Role role = Role.builder()
                .roleId(1L)
                .role("test")
                .build();

        when(roleRepository.findByRole(role.getRole())).thenReturn(Optional.of(role));

        // when
        RoleDto expected = roleService.findByRole(role.getRole());
        RoleDto actual = RoleDto.builder().role(role.getRole()).id(role.getRoleId()).build();

        // then
        assertEquals(expected, actual);
        verify(roleRepository).findByRole(role.getRole());
    }

    @Test
    public void givenRole_findRoleByRole_throwExceptionIfRoleDoesntExist() {
        // given
        Role role = Role.builder()
                .roleId(1L)
                .role("test")
                .build();

        when(roleRepository.findByRole(anyString())).thenReturn(Optional.empty());

        // when
        Executable call = () -> roleService.findByRole(role.getRole());

        // then
        assertThrows(RuntimeException.class, call);
    }

}
