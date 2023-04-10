package com.example.backend.service;

import com.example.backend.dto.UserDto;
import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import com.example.backend.exceptions.CrudOperationException;
import com.example.backend.repository.QuestionRepository;
import com.example.backend.repository.RoleRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.UserResultsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final QuestionRepository questionRepository;

    private final UserResultsRepository userResultsRepository;

    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, UserResultsRepository userResultsRepository, QuestionRepository questionRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userResultsRepository = userResultsRepository;
        this.questionRepository = questionRepository;
        this.roleRepository = roleRepository;
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

        return UserDto.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
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
//                            .userType(user.getRoles().iterator().next().getRole())
                            .build();
                    System.out.println("user type: " + user.getRoles().size());
                    if (user.getRoles().size() > 0) {
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

        return UserDto.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .userType(user.getRoles().iterator().next().getRole())
                .build();
    }

    public UserDto findUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });

        return UserDto.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .userType(user.getRoles().iterator().next().getRole())
                .build();
    }

    public UserDto findUserByUsernameOrEmail(String username, String email) {
        User user = userRepository.findByUsernameOrEmail(username, email).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });

        return UserDto.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .userType(user.getRoles().iterator().next().getRole())
                .build();
    }

    public Boolean userExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserDto assignRoleToUser(Long userId, Long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> {
            throw new CrudOperationException("Role does not exist!");
        });

        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new CrudOperationException("User does not exist");
        });

        if (user.getRoles() == null) {
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

    public void addQuestionScoreToStudent(Long studentId, Long questionId, double score) {

    }
}
