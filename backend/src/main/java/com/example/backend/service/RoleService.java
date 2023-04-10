package com.example.backend.service;

import com.example.backend.dto.RoleDto;
import com.example.backend.entity.Role;
import com.example.backend.exceptions.CrudOperationException;
import com.example.backend.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public RoleDto addRole(RoleDto roleDto) {
        Role role = Role.builder()
                .role(roleDto.getRole())
                .build();
        roleRepository.save(role);
        roleDto.setId(role.getRoleId());
        return roleDto;
    }

    public void removeRole(Long id) {
        roleRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Role does not exist!");
        });
        roleRepository.deleteById(id);
    }

    public RoleDto updateRole(Long id, RoleDto roleDto) {
        Role role = roleRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Role does not exist!");
        });

        role.setRole(roleDto.getRole());
        roleRepository.save(role);

        roleDto.setId(role.getRoleId());
        return roleDto;
    }

    public RoleDto getRoleById(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> {
            throw new CrudOperationException("Role does not exist!");
        });

        return RoleDto.builder()
                .id(role.getRoleId())
                .role(role.getRole())
                .build();
    }

    public List<RoleDto> getAllRoles() {
        Iterable<Role> roles = roleRepository.findAll();
        List<RoleDto> roleDtos = new ArrayList<>();

        roles.forEach(role ->
                roleDtos.add(RoleDto.builder()
                        .id(role.getRoleId())
                        .role(role.getRole())
                        .build()));
        return roleDtos;
    }

    public RoleDto findByRole(String givenRoleName) {
        Role role = roleRepository.findByRole(givenRoleName).orElseThrow(() -> {
            throw new CrudOperationException("Role does not exist!");
        });

        return RoleDto.builder()
                .id(role.getRoleId())
                .role(role.getRole())
                .build();
    }
}
