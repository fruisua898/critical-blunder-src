package com.criticalblunder.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleEnum {
    PLAYER,
    GAME_MASTER,
    ADMIN;
	
    @JsonCreator
    public static RoleEnum fromString(String value) {
        return RoleEnum.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toString() {
        return this.name();
    }
    
}

