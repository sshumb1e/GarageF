package com.example.garage_f.controller;

import com.example.garage_f.model.User;
import com.example.garage_f.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("prod")
public class AuthControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void shouldReturnLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("security/login"));
    }

    @Test
    public void shouldReturnRegistrationForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("security/register"));
    }

    @Test
    public void shouldOpenRegistrationPage_WhenTryToRegisterExistingEmail() throws Exception {
        when(userService.findByEmail(any())).thenReturn(Optional.of(User.builder().build()));

        mockMvc.perform(post("/register/save")
                        .flashAttr("user", User.builder().build()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register?error"));
    }

    @Test
    public void shouldRedirectToLoginPage_AfterRegistration() throws Exception {
        when(userService.findByEmail(any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/register/save")
                        .flashAttr("user", User.builder().build()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void shouldReturnListOfRegisteredUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("security/users"))
                .andExpect(model().attributeExists("users"));
    }
}
