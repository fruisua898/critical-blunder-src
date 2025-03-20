package com.criticalblunder.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.criticalblunder.dto.request.CampaignNoteRequestDTO;
import com.criticalblunder.enums.RoleEnum;
import com.criticalblunder.exception.CampaignNotFoundException;
import com.criticalblunder.exception.NoteNotFoundException;
import com.criticalblunder.exception.NotePermissionException;
import com.criticalblunder.model.CampaignNote;
import com.criticalblunder.service.CampaignNoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.criticalblunder.model.User;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import security.CustomUserDetails;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CampaignNoteControllerTest {

	@Mock
	private CampaignNoteService campaignNoteService;

	@InjectMocks
	private CampaignNoteController campaignNoteController;

	private User user;
	private CustomUserDetails userDetails;

	@BeforeEach
	void setUp() {
		user = new User();
		user.setId(1L);
		user.setEmail("test@example.com");
		user.setPassword("securepassword");
		user.setRole(RoleEnum.GAME_MASTER);

		userDetails = new CustomUserDetails(user);
	}

	@Test
	void createNoteOk() {
		CampaignNoteRequestDTO request = new CampaignNoteRequestDTO();
		request.setTitle("New Note");
		request.setContent("Content of the note.");

		CampaignNote note = new CampaignNote();
		note.setId(1L);
		note.setTitle("New Note");

		when(campaignNoteService.createNote(anyLong(), any(CampaignNoteRequestDTO.class), anyString()))
				.thenReturn(note);

		CampaignNote response = campaignNoteController.createNote(1L, request, userDetails);

		assertNotNull(response);
		assertEquals("New Note", response.getTitle());
	}

	@Test
	void createNoteCampaignNotFound() {
		when(campaignNoteService.createNote(anyLong(), any(CampaignNoteRequestDTO.class), anyString()))
				.thenThrow(new CampaignNotFoundException("Campaña no encontrada."));

		Exception exception = assertThrows(CampaignNotFoundException.class,
				() -> campaignNoteController.createNote(1L, new CampaignNoteRequestDTO(), userDetails));

		assertEquals("Campaña no encontrada.", exception.getMessage());
	}

	@Test
	void listNotesByCampaignOk() {
		CampaignNote note = new CampaignNote();
		note.setId(1L);
		note.setTitle("Note 1");

		when(campaignNoteService.getNotesByCampaign(anyLong())).thenReturn(List.of(note));

		List<CampaignNote> response = campaignNoteController.listNotesByCampaign(1L);

		assertFalse(response.isEmpty());
		assertEquals("Note 1", response.get(0).getTitle());
	}

	@Test
	void listNotesByCampaignEmpty() {
		when(campaignNoteService.getNotesByCampaign(anyLong())).thenReturn(Collections.emptyList());

		List<CampaignNote> response = campaignNoteController.listNotesByCampaign(1L);

		assertTrue(response.isEmpty());
	}

	@Test
	void updateNoteOk() {
		CampaignNoteRequestDTO request = new CampaignNoteRequestDTO();
		request.setTitle("Updated Note");
		request.setContent("Updated Content");

		CampaignNote updatedNote = new CampaignNote();
		updatedNote.setId(1L);
		updatedNote.setTitle("Updated Note");

		when(campaignNoteService.updateNote(anyLong(), any(CampaignNoteRequestDTO.class), anyString()))
				.thenReturn(updatedNote);

		CampaignNote response = campaignNoteController.updateNote(1L, request, userDetails);

		assertNotNull(response);
		assertEquals("Updated Note", response.getTitle());
	}

	@Test
	void updateNoteNotFound() {
		when(campaignNoteService.updateNote(anyLong(), any(CampaignNoteRequestDTO.class), anyString()))
				.thenThrow(new NoteNotFoundException("Nota no encontrada."));

		Exception exception = assertThrows(NoteNotFoundException.class,
				() -> campaignNoteController.updateNote(1L, new CampaignNoteRequestDTO(), userDetails));

		assertEquals("Nota no encontrada.", exception.getMessage());
	}

	@Test
	void updateNotePermissionDenied() {
		when(campaignNoteService.updateNote(anyLong(), any(CampaignNoteRequestDTO.class), anyString()))
				.thenThrow(new NotePermissionException("No tienes permisos para modificar esta nota."));

		Exception exception = assertThrows(NotePermissionException.class,
				() -> campaignNoteController.updateNote(1L, new CampaignNoteRequestDTO(), userDetails));

		assertEquals("No tienes permisos para modificar esta nota.", exception.getMessage());
	}

	@Test
	void deleteNoteOk() {
		doNothing().when(campaignNoteService).deleteNote(anyLong(), anyString());

		String response = campaignNoteController.deleteNote(1L, userDetails);

		assertEquals("Nota eliminada correctamente.", response);
	}

	@Test
	void deleteNoteNotFound() {
		doThrow(new NoteNotFoundException("Nota no encontrada.")).when(campaignNoteService).deleteNote(anyLong(),
				anyString());

		Exception exception = assertThrows(NoteNotFoundException.class,
				() -> campaignNoteController.deleteNote(1L, userDetails));

		assertEquals("Nota no encontrada.", exception.getMessage());
	}

	@Test
	void deleteNotePermissionDenied() {
		doThrow(new NotePermissionException("No tienes permisos para eliminar esta nota.")).when(campaignNoteService)
				.deleteNote(anyLong(), anyString());

		Exception exception = assertThrows(NotePermissionException.class,
				() -> campaignNoteController.deleteNote(1L, userDetails));

		assertEquals("No tienes permisos para eliminar esta nota.", exception.getMessage());
	}
}