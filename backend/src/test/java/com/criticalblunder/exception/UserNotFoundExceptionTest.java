package com.criticalblunder.exception;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class UserNotFoundExceptionTest {

	@Test
	void shouldCreateExceptionWithMessage() {
		String errorMessage = "Usuario no encontrado";
		UserNotFoundException exception = new UserNotFoundException(errorMessage);

		assertNotNull(exception);
		assertEquals(errorMessage, exception.getMessage());
	}

	@Test
	void shouldThrowUserNotFoundException() {
		Exception exception = assertThrows(UserNotFoundException.class, () -> {
			throw new UserNotFoundException("El usuario no existe en la base de datos");
		});

		assertEquals("El usuario no existe en la base de datos", exception.getMessage());
	}
}