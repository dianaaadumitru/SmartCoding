package com.example.backend.service;

import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    public void testLoadUserByUsername_UsernameFound() {
        // given
        String usernameOrEmail = "johndoe@example.com";
        String password = "password";
        String role = "ROLE_USER";

        User user = User.builder()
                .email("johndoe@example.com")
                .password(password)
                .build();

        Role userRole = new Role();
        userRole.setRole(role);
        user.setRoles(Collections.singleton(userRole));

        when(userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)).thenReturn(Optional.of(user));

        // when
        UserDetails userDetails = userDetailsService.loadUserByUsername(usernameOrEmail);

        // then
        assertEquals(usernameOrEmail, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        assertEquals(1, authorities.size());
        GrantedAuthority authority = authorities.iterator().next();
        assertEquals(role, authority.getAuthority());
    }

    @Test
    public void testLoadUserByUsername_UsernameNotFound() {
        // given
        String usernameOrEmail = "nonexistentUser";

        // when
        when(userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)).thenReturn(Optional.empty());

        // then
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(usernameOrEmail));
    }

}