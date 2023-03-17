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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private User testUser;

    @BeforeEach
    public void init() {
        userService = new UserService(userRepository,
                roleRepository,
                passwordEncoder);

        testUser = User.builder().name("Test user").build();
    }

    @Test
    public void saveTest() {
        Mockito.when(userRepository.save(any())).thenReturn(testUser);
        Mockito.when(roleRepository.save(any())).thenReturn(Role.builder().build());

        User expectedUser = userService.saveUser(User.builder().build(), any());
        Assertions.assertEquals(expectedUser, testUser);
    }

    @Test
    public void findByExistedEmail_shouldReturnNotEmptyValue() {
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.of(testUser));
        Assertions.assertTrue(userService.findByEmail(any()).isPresent());
    }

    @Test
    public void findByNotExistedEmail_shouldReturnEmptyValue() {
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        Assertions.assertTrue(userService.findByEmail(any()).isEmpty());
    }

    @Test
    public void getAllTest() {
        List<User> expectedUsers = List.of(
                User.builder().name("User 1").build(),
                User.builder().name("User 2").build(),
                User.builder().name("User 3").build()
        );

        Mockito.when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> users = userService.findAll();
        Assertions.assertEquals(users, expectedUsers);
    }
}
