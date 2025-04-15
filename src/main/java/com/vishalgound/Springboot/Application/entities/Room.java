package com.vishalgound.Springboot.Application.entities;

import java.util.List;
import java.util.Map;

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
public class Room {
    private Long id;
    private String roomNumber;
    private List<RoleTypeEnum> roomRoles;
    private Map<RoleTypeEnum, List<String>> roomAccessTime; // enhance
    private FlagTypeEnum isAvailable;
    private Long startTimeInMillis;
    private Long endTimeInMillis;
    private String createdBy;
    private String updatedBy;
}
