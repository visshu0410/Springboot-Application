package com.vishalgound.Springboot.Application.api.impl;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishalgound.Springboot.Application.constants.CommonConstants;
import com.vishalgound.Springboot.Application.entities.Role;
import com.vishalgound.Springboot.Application.enums.RoleTypeEnum;

class RolesApiImplTest {

    private RolesApiImpl rolesApi;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        rolesApi = new RolesApiImpl();
        mockMvc = MockMvcBuilders.standaloneSetup(rolesApi).build();
    }

    @Test
    void testCreateRoleSuccess() throws Exception {
        Role role = new Role();
        role.setCreatedBy(CommonConstants.ADMIN);
        role.setRoleType(RoleTypeEnum.MANAGER);

        mockMvc.perform(post("/roles/create").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(role))).andExpect(status().isOk())
                .andExpect(content().string(containsString("Role created successfully with ID")));
    }

    @Test
    void testCreateRoleFails_NullRole() throws Exception {
        mockMvc.perform(post("/roles/create").contentType(MediaType.APPLICATION_JSON).content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateRoleFails_NotCreatedByAdmin() throws Exception {
        Role role = new Role();
        role.setCreatedBy("User1");
        role.setRoleType(RoleTypeEnum.HR);

        mockMvc.perform(post("/roles/create").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(role))).andExpect(status().isOk())
                .andExpect(content().string(containsString("Only Admins can create roles")));
    }

    @Test
    void testCreateRoleFails_NullRoleType() throws Exception {
        Role role = new Role();
        role.setCreatedBy(CommonConstants.ADMIN);
        role.setRoleType(null);

        mockMvc.perform(post("/roles/create").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(role))).andExpect(status().isOk())
                .andExpect(content().string(containsString("Role creation failed: Role type must be specified")));
    }
}
