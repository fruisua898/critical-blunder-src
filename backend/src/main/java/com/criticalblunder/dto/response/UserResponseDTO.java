package com.criticalblunder.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import com.criticalblunder.enums.RoleEnum;

import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private RoleEnum role;
}
