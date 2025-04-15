package com.vishalgound.Springboot.Application.api.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vishalgound.Springboot.Application.constants.CommonConstants;
import com.vishalgound.Springboot.Application.entities.Room;
import com.vishalgound.Springboot.Application.entities.User;
import com.vishalgound.Springboot.Application.enums.FlagTypeEnum;
import com.vishalgound.Springboot.Application.enums.RoleTypeEnum;

@RestController
@RequestMapping("/rooms")
public class RoomsApiImpl {

    private final Map<Long, Room> roomDb = new HashMap<>();
    private Long roomIdCounter = 1L;

    @Autowired
    public UserApiImpl userApi;

    @PostMapping("/add")
    public String createRoom(@RequestBody Room room) {
        if (Objects.isNull(room) || !CommonConstants.ADMIN.equals(room.getCreatedBy())) {
            return "Room creation failed: Only Admins can create rooms.";
        }

        room.setId(roomIdCounter++);
        room.setStartTimeInMillis(System.currentTimeMillis());
        room.setEndTimeInMillis(System.currentTimeMillis() + CommonConstants.ONE_HOUR_LATER);

        roomDb.put(room.getId(), room);

        return "Room created successfully with ID: " + room.getId();
    }

    @GetMapping("/access")
    public String accessRoom(@RequestParam Long userId, @RequestParam Long roomId, @RequestParam RoleTypeEnum userRole,
            @RequestParam String keyCard) {

        User user = userApi.userDb.get(userId);
        if (Objects.isNull(user)) {
            return "Access denied: User not found.";
        }

        if (FlagTypeEnum.Y.equals(user.getIsBlocked())) {
            return "Access denied: User is blocked (ID: " + user.getId() + ", Name: " + user.getName() + ").";
        }

        if (!Objects.equals(user.getKeyIdCard(), keyCard)) {
            user.setIsBlocked(FlagTypeEnum.Y);
            return "Access denied: Invalid key card. User (ID: " + user.getId() + ", Name: " + user.getName()
                    + ") is now blocked.";
        }

        if (Objects.isNull(user.getRoles()) || !user.getRoles().contains(userRole)) {
            user.setIsBlocked(FlagTypeEnum.Y);
            return "Access denied: User does not have the required role. User (ID: " + user.getId() + ", Name: "
                    + user.getName() + ") is now blocked.";
        }

        Room room = roomDb.get(roomId);
        if (Objects.isNull(room)) {
            user.setIsBlocked(FlagTypeEnum.Y);
            return "Access denied: Room not found. User (ID: " + user.getId() + ", Name: " + user.getName()
                    + ") is now blocked.";
        }

        if (Objects.isNull(room.getRoomRoles()) || !room.getRoomRoles().contains(userRole)) {
            user.setIsBlocked(FlagTypeEnum.Y);
            return "Access denied: Room is not accessible for the given role. User (ID: " + user.getId() + ", Name: "
                    + user.getName() + ") is now blocked.";
        }

        return "Success @ " + System.currentTimeMillis() + " → Access granted to room ID: " + room.getId()
                + " for user ID: " + user.getId() + " with role: " + userRole;
    }
}
