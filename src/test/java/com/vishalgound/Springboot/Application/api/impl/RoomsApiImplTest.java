package com.vishalgound.Springboot.Application.api.impl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishalgound.Springboot.Application.constants.CommonConstants;
import com.vishalgound.Springboot.Application.entities.Room;
import com.vishalgound.Springboot.Application.entities.User;
import com.vishalgound.Springboot.Application.enums.FlagTypeEnum;
import com.vishalgound.Springboot.Application.enums.RoleTypeEnum;

@WebMvcTest(RoomsApiImpl.class)
public class RoomsApiImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserApiImpl userApi;

    private Room room;
    private User user;

    @BeforeEach
    public void setup() {
        room = new Room();
        room.setCreatedBy(CommonConstants.ADMIN);
        room.setRoomRoles(List.of(RoleTypeEnum.MANAGER));

        user = new User();
        user.setId(1L);
        user.setIsBlocked(FlagTypeEnum.N);
        user.setKeyIdCard("NAVIJohn1");
        user.setRoles(List.of(RoleTypeEnum.MANAGER));
    }

    @Test
    void testCreateRoomSuccess() throws Exception {
        room.setId(null);

        mockMvc.perform(post("/rooms/add").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(room))).andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Room created successfully")));
    }

    @Test
    void testCreateRoomByNonAdminFails() throws Exception {
        room.setCreatedBy("GUEST");

        mockMvc.perform(post("/rooms/add").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(room))).andExpect(status().isOk())
                .andExpect(content().string("Room creation failed: Only Admins can create rooms."));
    }

    @Test
    void testAccessRoomSuccess() throws Exception {
        Map<Long, User> fakeUserDb = new HashMap<>();
        fakeUserDb.put(1L, user);

        ReflectionTestUtils.setField(userApi, "userDb", fakeUserDb);
        room.setId(10L);

        RoomsApiImpl api = new RoomsApiImpl();
        api.userApi = userApi;
        api.createRoom(room);

        mockMvc.perform(get("/rooms/access").param("userId", "1").param("roomId", "1").param("userRole", "MANAGER")
                .param("keyCard", "NAVIJohn1")).andExpect(status().isOk());
    }

    @Test
    void testAccessRoomFails_UserBlocked() throws Exception {
        user.setId(1L);
        user.setName("John");
        user.setCreatedBy(CommonConstants.ADMIN);
        user.setKeyIdCard("NAVIJohn1");
        user.setIsBlocked(FlagTypeEnum.Y);
        user.setRoles(Collections.singletonList(RoleTypeEnum.MANAGER));

        Map<Long, User> fakeUserDb = new HashMap<>();
        fakeUserDb.put(1L, user);

        ReflectionTestUtils.setField(userApi, "userDb", fakeUserDb);

        mockMvc.perform(get("/rooms/access").param("userId", "1").param("roomId", "1").param("userRole", "MANAGER")
                .param("keyCard", "NAVIJohn1")).andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("User is blocked")));
    }

    @Test
    void testAccessRoomFails_InvalidKeyCard() throws Exception {
        User invalidUser = new User();
        invalidUser.setId(1L);
        invalidUser.setName("John");
        invalidUser.setKeyIdCard("WRONGCARD");
        invalidUser.setIsBlocked(FlagTypeEnum.N);
        invalidUser.setRoles(List.of(RoleTypeEnum.MANAGER));

        Map<Long, User> fakeUserDb = new HashMap<>();
        fakeUserDb.put(1L, invalidUser);

        ReflectionTestUtils.setField(userApi, "userDb", fakeUserDb);

        mockMvc.perform(get("/rooms/access").param("userId", "1").param("roomId", "1").param("userRole", "MANAGER")
                .param("keyCard", "NAVIJohn1")).andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Invalid key card")));
    }

    @Test
    void testAccessRoomFails_InvalidRole() throws Exception {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setName("John");
        testUser.setKeyIdCard("NAVIJohn1");
        testUser.setIsBlocked(FlagTypeEnum.N);
        testUser.setRoles(Collections.singletonList(RoleTypeEnum.HR));

        Map<Long, User> fakeUserDb = new HashMap<>();
        fakeUserDb.put(1L, testUser);
        ReflectionTestUtils.setField(userApi, "userDb", fakeUserDb);

        Room testRoom = new Room();
        testRoom.setId(1L);
        testRoom.setRoomRoles(Collections.singletonList(RoleTypeEnum.MANAGER));

        mockMvc.perform(get("/rooms/access").param("userId", "1").param("roomId", "1").param("userRole", "MANAGER")
                .param("keyCard", "NAVIJohn1")).andExpect(status().isOk()).andExpect(
                        content().string(org.hamcrest.Matchers.containsString("User does not have the required role")));
    }
}
