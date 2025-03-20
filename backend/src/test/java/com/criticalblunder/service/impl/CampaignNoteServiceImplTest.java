package com.criticalblunder.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.criticalblunder.dto.request.CampaignNoteRequestDTO;
import com.criticalblunder.enums.RoleEnum;
import com.criticalblunder.exception.CampaignNotFoundException;
import com.criticalblunder.exception.NoteNotFoundException;
import com.criticalblunder.exception.NotePermissionException;
import com.criticalblunder.exception.UserNotFoundException;
import com.criticalblunder.model.Campaign;
import com.criticalblunder.model.CampaignNote;
import com.criticalblunder.model.User;
import com.criticalblunder.repository.CampaignNoteRepository;
import com.criticalblunder.repository.CampaignRepository;
import com.criticalblunder.repository.HeroRepository;
import com.criticalblunder.repository.UserRepository;
import com.criticalblunder.service.CampaignNoteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CampaignNoteServiceImplTest {

	@Mock
	private CampaignNoteRepository campaignNoteRepository;

	@Mock
	private CampaignRepository campaignRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private HeroRepository heroRepository;

	@InjectMocks
	private CampaignNoteServiceImpl campaignNoteService;

	private User user;
	private Campaign campaign;
	private CampaignNote note;
	private CampaignNoteRequestDTO requestDTO;

	@BeforeEach
	void setUp() {
		user = new User();
		user.setId(1L);
		user.setName("Test User");
		user.setEmail("test@example.com");

		campaign = new Campaign();
		campaign.setId(1L);
		campaign.setGameMaster(user);

		note = new CampaignNote();
		note.setId(1L);
		note.setTitle("Sample Note");
		note.setContent("Sample Content");
		note.setAuthor("Test User");
		note.setCampaign(campaign);

		requestDTO = new CampaignNoteRequestDTO();
		requestDTO.setTitle("New Note Title");
		requestDTO.setContent("New Note Content");
	}

	@Test
	void createNoteOk() {
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
		when(campaignRepository.findById(campaign.getId())).thenReturn(Optional.of(campaign));
		when(heroRepository.existsHeroInCampaignByUserId(user.getId(), campaign.getId())).thenReturn(true);
		when(campaignNoteRepository.save(any(CampaignNote.class))).thenReturn(note);

		CampaignNote createdNote = campaignNoteService.createNote(campaign.getId(), requestDTO, user.getEmail());

		assertNotNull(createdNote);
		assertEquals("Sample Note", createdNote.getTitle());
	}

	@Test
	void createNoteUserNotFound() {
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class,
				() -> campaignNoteService.createNote(campaign.getId(), requestDTO, user.getEmail()));
	}

	@Test
	void createNoteCampaignNotFound() {
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
		when(campaignRepository.findById(campaign.getId())).thenReturn(Optional.empty());

		assertThrows(CampaignNotFoundException.class,
				() -> campaignNoteService.createNote(campaign.getId(), requestDTO, user.getEmail()));
	}

	@Test
	void createNoteNoPermission() {
		User normalUser = new User();
		normalUser.setId(2L);
		normalUser.setRole(RoleEnum.PLAYER);
		when(userRepository.findByEmail(normalUser.getEmail())).thenReturn(Optional.of(normalUser));

		User gameMaster = new User();
		gameMaster.setId(1L);
		gameMaster.setRole(RoleEnum.GAME_MASTER);

		Campaign campaign = mock(Campaign.class);
		when(campaign.getId()).thenReturn(1L);
		lenient().when(campaign.getGameMaster()).thenReturn(gameMaster);
		when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaign));

		when(heroRepository.existsHeroInCampaignByUserId(normalUser.getId(), campaign.getId())).thenReturn(false);

		assertThrows(NotePermissionException.class,
				() -> campaignNoteService.createNote(campaign.getId(), requestDTO, normalUser.getEmail()));
	}

	@Test
	void getNotesByCampaignOk() {
		when(campaignNoteRepository.findByCampaignId(campaign.getId())).thenReturn(Collections.singletonList(note));

		List<CampaignNote> notes = campaignNoteService.getNotesByCampaign(campaign.getId());

		assertFalse(notes.isEmpty());
		assertEquals(1, notes.size());
	}

	@Test
	void updateNoteOk() {
		when(campaignNoteRepository.findById(note.getId())).thenReturn(Optional.of(note));
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
		when(campaignNoteRepository.save(any(CampaignNote.class))).thenReturn(note);

		CampaignNote updatedNote = campaignNoteService.updateNote(note.getId(), requestDTO, user.getEmail());

		assertNotNull(updatedNote);
		assertEquals(requestDTO.getTitle(), updatedNote.getTitle());
		assertEquals(requestDTO.getContent(), updatedNote.getContent());
	}

	@Test
	void updateNoteNotFound() {
		when(campaignNoteRepository.findById(note.getId())).thenReturn(Optional.empty());

		assertThrows(NoteNotFoundException.class,
				() -> campaignNoteService.updateNote(note.getId(), requestDTO, user.getEmail()));
	}

	@Test
	void updateNoteUserNotFound() {
		when(campaignNoteRepository.findById(note.getId())).thenReturn(Optional.of(note));
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class,
				() -> campaignNoteService.updateNote(note.getId(), requestDTO, user.getEmail()));
	}

	@Test
	void updateNoteNoPermission() {
		User anotherUser = new User();
		anotherUser.setId(2L);
		anotherUser.setName("Another User");

		when(campaignNoteRepository.findById(note.getId())).thenReturn(Optional.of(note));
		when(userRepository.findByEmail(anotherUser.getEmail())).thenReturn(Optional.of(anotherUser));

		assertThrows(NotePermissionException.class,
				() -> campaignNoteService.updateNote(note.getId(), requestDTO, anotherUser.getEmail()));
	}

	@Test
	void deleteNoteOk() {
		when(campaignNoteRepository.findById(note.getId())).thenReturn(Optional.of(note));
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
		doNothing().when(campaignNoteRepository).delete(any(CampaignNote.class));

		assertDoesNotThrow(() -> campaignNoteService.deleteNote(note.getId(), user.getEmail()));
	}

	@Test
	void deleteNoteNotFound() {
		when(campaignNoteRepository.findById(note.getId())).thenReturn(Optional.empty());

		assertThrows(NoteNotFoundException.class, () -> campaignNoteService.deleteNote(note.getId(), user.getEmail()));
	}

	@Test
	void deleteNoteUserNotFound() {
		when(campaignNoteRepository.findById(note.getId())).thenReturn(Optional.of(note));
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> campaignNoteService.deleteNote(note.getId(), user.getEmail()));
	}

	@Test
	void deleteNoteNoPermission() {
		User anotherUser = new User();
		anotherUser.setId(2L);
		anotherUser.setName("Another User");

		when(campaignNoteRepository.findById(note.getId())).thenReturn(Optional.of(note));
		when(userRepository.findByEmail(anotherUser.getEmail())).thenReturn(Optional.of(anotherUser));

		assertThrows(NotePermissionException.class,
				() -> campaignNoteService.deleteNote(note.getId(), anotherUser.getEmail()));
	}
}
