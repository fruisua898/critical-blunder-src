package com.criticalblunder.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.criticalblunder.enums.HeroClassEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HeroRequestDTO {

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres.")
    private String name;

    @NotNull(message = "La clase del héroe es obligatoria.")
    private HeroClassEnum heroClass;

    @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres.")
    private String description;

    private Integer age;

    @Size(max = 255, message = "La apariencia no puede superar los 255 caracteres.")
    private String appearance;
}