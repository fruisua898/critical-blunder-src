package com.criticalblunder.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

	@NotBlank(message = "El nombre es obligatorio.")
	@Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres.")
	private String name;

	@NotBlank(message = "El correo es obligatorio.")
	@Email(message = "Debe proporcionar un correo electrónico válido.")
	private String email;

	@NotBlank(message = "La contraseña es obligatoria.")
	@Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
	private String password;
}
