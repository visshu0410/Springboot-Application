package com.vishalgound.Springboot.Application.api.impl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishalgound.Springboot.Application.constants.CommonConstants;
import com.vishalgound.Springboot.Application.entities.User;

@WebMvcTest(UserApiImpl.class)
public class UserApiImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateUser_Success() throws Exception {
        User user = new User();
        user.setName("John");
        user.setCreatedBy(CommonConstants.ADMIN);

        mockMvc.perform(post("/users/create").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))).andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("User created successfully")));
    }

    @Test
    void testCreateUser_NotAdmin() throws Exception {
        User user = new User();
        user.setName("Alice");
        user.setCreatedBy("GUEST");

        mockMvc.perform(post("/users/create").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))).andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Only admin can create users")));
    }

    @Test
    void testGetAllUsers_EmptyInitially() throws Exception {
        mockMvc.perform(get("/users/getAll")).andExpect(status().isOk()).andExpect(content().json("[]"));
    }
}
