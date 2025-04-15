package com.vishalgound.Springboot.Application.api.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vishalgound.Springboot.Application.constants.CommonConstants;
import com.vishalgound.Springboot.Application.entities.Role;

@RestController
@RequestMapping("/roles")
public class RolesApiImpl {

    Map<Long, Role> roleDb = new HashMap<>();
    private Long roleIdCounter = 1L;

    @PostMapping("/create")
    public String createRole(@RequestBody Role role) {
        if (Objects.isNull(role) || !CommonConstants.ADMIN.equals(role.getCreatedBy())) {
            return "Role creation failed: Only Admins can create roles.";
        }

        if (Objects.isNull(role.getRoleType())) {
            return "Role creation failed: Role type must be specified.";
        }

        role.setId(roleIdCounter++);
        roleDb.put(role.getId(), role);

        return "Role created successfully with ID: " + role.getId() + " and type: " + role.getRoleType();
    }
}
