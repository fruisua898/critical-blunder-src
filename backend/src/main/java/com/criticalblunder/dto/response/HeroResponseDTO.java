package com.criticalblunder.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeroResponseDTO {
    private Long id;
    private String userName;
    private String name;
    private String description;
    private Integer age;
    private String appearance;
    private String heroClass;
    private String level;
    private String status;
}
