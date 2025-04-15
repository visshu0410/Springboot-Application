package com.vishalgound.Springboot.Application.entities;

import java.util.List;

import com.vishalgound.Springboot.Application.enums.FlagTypeEnum;
import com.vishalgound.Springboot.Application.enums.RoleTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private String keyIdCard;
    private FlagTypeEnum isBlocked = FlagTypeEnum.N;
    private List<RoleTypeEnum> roles;
    private String createdBy;
    private String updatedBy;
}
