package com.vishalgound.Springboot.Application.api.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import com.vishalgound.Springboot.Application.entities.User;

import javax.annotation.meta.When;
import java.util.Map;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApiImplTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Mock
    private Map<String, User> userData;

    @InjectMocks
    private UserApiImpl userApi;

    @Test
    public void addUser_test() {
        User user = new User();
        user.setMobileNumber(9876543210L);
        user.setEmail("test@example.com");
        user.setAge(25);
        user.setPassword("123");

        String response = userApi.addUser(user);

        assertTrue(response.contains("User added with ID:"));
    }

    @Test
    public void addUserAlreadyExist_test() {
        User user = new User();
        user.setMobileNumber(9876543210L);
        user.setEmail("test@example.com");
        user.setAge(25);
        user.setPassword("123");

        String key = user.getMobileNumber() + "$" + user.getEmail();
        when(userData.get(key)).thenReturn(user);

        String response = userApi.addUser(user);

        assertEquals("User Already Exists", response);

    }
}
