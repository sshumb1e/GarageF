package com.example.garage_f.service;

import com.example.garage_f.model.Role;
import com.example.garage_f.model.User;
import com.example.garage_f.repository.RoleRepository;
import com.example.garage_f.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
public class UserDetailService {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;

    private CustomUserDetailService customUserDetailsService;

    @BeforeEach
    public void init() {
        customUserDetailsService = new CustomUserDetailService(userRepository, roleRepository);
    }

    @Test
    public void givenExistingEmail_then_return_UserDetails() {
        User testUser = User.builder()
                .email("test@email")
                .password("password")
                .build();
        List<Role> roles = List.of(Role.builder().role("USER").build());

        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.of(testUser));
        Mockito.when(roleRepository.findAllByUserId(anyInt())).thenReturn(roles);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(any());

        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals(testUser.getEmail(), userDetails.getUsername());
    }

    @Test
    public void givenNotExistingEmail_then_throw_Exception() {
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        UsernameNotFoundException thrownException = Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(any());
        });

        Assertions.assertEquals("Invalid username or password.", thrownException.getMessage());
    }

}
