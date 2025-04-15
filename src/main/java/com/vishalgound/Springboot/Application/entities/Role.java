package com.vishalgound.Springboot.Application.entities;

import com.vishalgound.Springboot.Application.enums.RoleTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private Long id;
    private RoleTypeEnum roleType;
    private String createdBy;
    private String updatedBy;
}
