package com.vishalgound.Springboot.Application.api.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vishalgound.Springboot.Application.entities.User;

import jakarta.ws.rs.QueryParam;

@RestController
@RequestMapping("/User")
public class UserApiImpl {
    public Map<String, User> userData = new HashMap<>();

    private Long nextUserId = 10L;

    @PostMapping("/addUser")
    public String addUser(@RequestBody User user) {
        String key = user.getMobileNumber() + "$" + user.getEmail();
        if (Objects.nonNull(userData.get(key))) {
            return "User Already Exists";
        }

        if (user.getId() == null) {
            user.setId(nextUserId);
            nextUserId++;
        }

        userData.put(key, user);

        return "User added with ID: " + user.getId();
    }

    @GetMapping("/getUser")
    public User getUserByEmailIdAndMobileNumber(@QueryParam("mobileNumber") String mobileNumber,
            @QueryParam("email") String email) {
        String key = mobileNumber + "$" + email;

        if (Objects.isNull(userData.get(key))) {
            return null;
        }
        return userData.get(key);
    }

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return new ArrayList<>(userData.values());
    }

    @PatchMapping("/update/{id}")
    public User updateUser(@PathVariable Long id, @QueryParam("mobile") String mobileNumber,
            @QueryParam("email") String email, @RequestBody Long newMobileNumber) {
        String key = mobileNumber + "$" + email;
        User user = userData.get(key);

        if (Objects.isNull(user)) {
            return null;
        }
        userData.remove(key);
        user.setMobileNumber(newMobileNumber);
        String newKey = newMobileNumber.toString() + "$" + user.getEmail();
        userData.put(newKey, user);

        return user;
    }

    @DeleteMapping("/delete/User/{id}")
    public String deleteUser(@PathVariable Long id, @QueryParam("mobile") String mobileNumber,
            @QueryParam("email") String email) {
        String key = mobileNumber + "$" + email;
        User user = userData.get(key);
        if (Objects.isNull(user)) {
            return "User Does Not Exists";
        }
        userData.remove(key);
        return "User Deleted Successfully";
    }

}
