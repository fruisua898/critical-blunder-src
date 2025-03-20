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
public class CampaignNoteRequestDTO {

    @NotBlank(message = "El título de la nota es obligatorio.")
    @Size(max = 255, message = "El título de la nota no puede tener más de 255 caracteres.")
    private String title;

    @NotBlank(message = "El contenido de la nota es obligatorio.")
    @Size(max = 1000, message = "El contenido de la nota no puede tener más de 1000 caracteres.")
    private String content;

}
