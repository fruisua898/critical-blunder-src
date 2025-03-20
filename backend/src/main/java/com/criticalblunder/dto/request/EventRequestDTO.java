package com.criticalblunder.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDTO {

    @NotBlank(message = "El título del evento es obligatorio.")
    @Size(max = 255, message = "El título del evento no puede tener más de 255 caracteres.")
    private String title;

    @NotBlank(message = "La descripción del evento es obligatoria.")
    @Size(max = 1000, message = "La descripción del evento no puede tener más de 1000 caracteres.")
    private String description;

}
