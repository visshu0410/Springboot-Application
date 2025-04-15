package com.vishalgound.Springboot.Application.api.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vishalgound.Springboot.Application.constants.CommonConstants;
import com.vishalgound.Springboot.Application.entities.User;

@RestController
@RequestMapping("/users")
public class UserApiImpl {

    Map<Long, User> userDb = new HashMap<>();
    private Long userIdCounter = 1L;

    @PostMapping("/create")
    public String createUser(@RequestBody User user) {
        boolean userExists = userDb.values().stream()
                .anyMatch(existingUser -> existingUser.getEmail().equalsIgnoreCase(user.getEmail()));

        if (userExists) {
            return "User creation failed: User with this email already exists.";
        }

        if (Objects.isNull(user)) {
            return "User creation failed: Request body is empty.";
        }

        if (!CommonConstants.ADMIN.equals(user.getCreatedBy())) {
            return "User creation failed: Only admin can create users.";
        }

        user.setId(userIdCounter++);
        user.setKeyIdCard(CommonConstants.NAVI + user.getName() + user.getId());
        userDb.put(user.getId(), user);

        return "User created successfully with ID: " + user.getId();
    }

    @GetMapping("/getAll")
    public List<User> getAllUsers() {
        return new ArrayList<>(userDb.values());
    }
}