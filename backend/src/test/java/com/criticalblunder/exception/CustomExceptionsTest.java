package com.criticalblunder.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CustomExceptionsTest {

	@Test
	void shouldThrowUserNotFoundException() {
		Exception exception = assertThrows(UserNotFoundException.class, () -> {
			throw new UserNotFoundException("Usuario no encontrado.");
		});

		assertEquals("Usuario no encontrado.", exception.getMessage());
	}

	@Test
	void shouldThrowDuplicateCampaignNameException() {
		Exception exception = assertThrows(DuplicateCampaignNameException.class, () -> {
			throw new DuplicateCampaignNameException("Nombre de campaña duplicado.");
		});

		assertEquals("Nombre de campaña duplicado.", exception.getMessage());
	}

	@Test
	void shouldThrowCampaignNotFoundException() {
		Exception exception = assertThrows(CampaignNotFoundException.class, () -> {
			throw new CampaignNotFoundException("Campaña no encontrada.");
		});

		assertEquals("Campaña no encontrada.", exception.getMessage());
	}

	@Test
	void shouldThrowDuplicateUserException() {
		Exception exception = assertThrows(DuplicateUserException.class, () -> {
			throw new DuplicateUserException("Usuario ya registrado.");
		});

		assertEquals("Usuario ya registrado.", exception.getMessage());
	}

	@Test
	void shouldThrowNoteNotFoundException() {
		Exception exception = assertThrows(NoteNotFoundException.class, () -> {
			throw new NoteNotFoundException("Nota no encontrada.");
		});

		assertEquals("Nota no encontrada.", exception.getMessage());
	}

	@Test
	void shouldThrowNotePermissionException() {
		Exception exception = assertThrows(NotePermissionException.class, () -> {
			throw new NotePermissionException("No tienes permisos para esta nota.");
		});

		assertEquals("No tienes permisos para esta nota.", exception.getMessage());
	}

	@Test
	void shouldThrowInvalidCredentialsException() {
		Exception exception = assertThrows(InvalidCredentialsException.class, () -> {
			throw new InvalidCredentialsException("Credenciales inválidas.");
		});

		assertEquals("Credenciales inválidas.", exception.getMessage());
	}

	@Test
	void shouldCreateExceptionWithMessage() {
		String errorMessage = "Usuario no encontrado";
		UserNotFoundException exception = new UserNotFoundException(errorMessage);

		assertNotNull(exception);
		assertEquals(errorMessage, exception.getMessage());
	}

}