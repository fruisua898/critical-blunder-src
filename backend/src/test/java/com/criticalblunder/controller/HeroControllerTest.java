package com.criticalblunder.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.criticalblunder.dto.request.HeroRequestDTO;
import com.criticalblunder.dto.request.HeroRequestUpdateDTO;
import com.criticalblunder.dto.response.HeroResponseDTO;
import com.criticalblunder.model.Campaign;
import com.criticalblunder.model.User;
import com.criticalblunder.service.HeroService;
import com.criticalblunder.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import security.CustomUserDetails;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class HeroControllerTest {

	@Mock
	private HeroService heroService;

	@Mock
	private UserService userService;

	@InjectMocks
	private HeroController heroController;

	private CustomUserDetails userDetails;
	private User user;

	@BeforeEach
	void setUp() {
		user = new User();
		user.setId(1L);
		user.setEmail("player@example.com");
		userDetails = mock(CustomUserDetails.class);
		lenient().when(userDetails.getUsername()).thenReturn("player@example.com");
	}

	@Test
	void createHeroOk() {
		HeroRequestDTO request = new HeroRequestDTO();
		request.setName("Warrior");

		when(userService.getUserOrThrow(anyString())).thenReturn(user);
		doNothing().when(heroService).createHero(any(HeroRequestDTO.class), any(User.class));

		ResponseEntity<?> response = heroController.createHero(request, userDetails);
		assertEquals(200, response.getStatusCode().value());
		assertEquals("Héroe creado satisfactoriamente.", response.getBody());
	}

	@Test
	void deleteHeroOk() {
		when(userService.getUserOrThrow(anyString())).thenReturn(user);
		doNothing().when(heroService).deleteHero(anyLong(), any(User.class));

		ResponseEntity<?> response = heroController.deleteHero(1L, userDetails);
		assertEquals(200, response.getStatusCode().value());
		assertEquals("Héroe eliminado correctamente.", response.getBody());
	}

	@Test
	void deleteHeroPermissionDenied() {
		when(userService.getUserOrThrow(anyString())).thenReturn(user);
		doThrow(new SecurityException("No puedes eliminar héroes que no te pertenecen.")).when(heroService)
				.deleteHero(anyLong(), any(User.class));

		Exception exception = assertThrows(SecurityException.class, () -> heroController.deleteHero(1L, userDetails));

		assertEquals("No puedes eliminar héroes que no te pertenecen.", exception.getMessage());
	}

	@Test
	void listCampaignsForHeroOk() {
		Campaign campaign = new Campaign();
		campaign.setId(1L);
		campaign.setName("Epic Campaign");

		when(heroService.getCampaignsForHero(anyLong())).thenReturn(List.of(campaign));

		ResponseEntity<List<Campaign>> response = heroController.listCampaignsForHero(1L);
		assertFalse(response.getBody().isEmpty());
		assertEquals("Epic Campaign", response.getBody().get(0).getName());
	}

	@Test
	void leaveCampaignOk() {
		when(userService.getUserOrThrow(anyString())).thenReturn(user);
		doNothing().when(heroService).leaveCampaign(anyLong(), anyLong(), any(User.class));

		ResponseEntity<?> response = heroController.leaveCampaign(1L, 2L, userDetails);
		assertEquals(200, response.getStatusCode().value());
		assertEquals("Héroe retirado de la campaña.", response.getBody());
	}

	@Test
	void leaveCampaignPermissionDenied() {
		when(userService.getUserOrThrow(anyString())).thenReturn(user);
		doThrow(new SecurityException("No puedes retirar un héroe que no te pertenece.")).when(heroService)
				.leaveCampaign(anyLong(), anyLong(), any(User.class));

		Exception exception = assertThrows(SecurityException.class,
				() -> heroController.leaveCampaign(1L, 2L, userDetails));

		assertEquals("No puedes retirar un héroe que no te pertenece.", exception.getMessage());
	}

	@Test
	void updateHeroOk() {
		HeroRequestUpdateDTO request = new HeroRequestUpdateDTO();
		request.setName("Updated Name");

		when(userService.getUserOrThrow(anyString())).thenReturn(user);
		doNothing().when(heroService).validateHeroOwnership(anyLong(), any(User.class));
		doNothing().when(heroService).updateHero(anyLong(), any(HeroRequestUpdateDTO.class), any(User.class));

		ResponseEntity<?> response = heroController.updateHero(1L, request, userDetails);
		assertEquals(200, response.getStatusCode().value());
		assertEquals("Héroe actualizado correctamente.", response.getBody());
	}

	@Test
	void getUserHeroesOk() {
		HeroResponseDTO hero = new HeroResponseDTO();
		hero.setId(1L);
		hero.setName("Mage");

		when(userService.getUserOrThrow(anyString())).thenReturn(user);
		when(heroService.getUserHeroes(any(User.class))).thenReturn(List.of(hero));

		ResponseEntity<List<HeroResponseDTO>> response = heroController.getUserHeroes(userDetails);
		assertFalse(response.getBody().isEmpty());
		assertEquals("Mage", response.getBody().get(0).getName());
	}

	@Test
	void getUserHeroesNoContent() {
		when(userService.getUserOrThrow(anyString())).thenReturn(user);
		when(heroService.getUserHeroes(any(User.class))).thenReturn(Collections.emptyList());

		ResponseEntity<List<HeroResponseDTO>> response = heroController.getUserHeroes(userDetails);
		assertEquals(204, response.getStatusCode().value());
	}

	@Test
	void getHeroOk() {
		HeroResponseDTO hero = new HeroResponseDTO();
		hero.setId(1L);
		hero.setName("Rogue");

		when(userService.getUserOrThrow(anyString())).thenReturn(user);
		doNothing().when(heroService).validateHeroOwnership(anyLong(), any(User.class));
		when(heroService.getHeroResponse(anyLong())).thenReturn(hero);

		ResponseEntity<HeroResponseDTO> response = heroController.getHero(1L, userDetails);
		assertEquals("Rogue", response.getBody().getName());
	}

	@Test
	void getHeroesByUserIdOk() {
		HeroResponseDTO hero = new HeroResponseDTO();
		hero.setId(1L);
		hero.setName("Paladin");

		when(heroService.getHeroesByUserId(anyLong())).thenReturn(List.of(hero));

		ResponseEntity<List<HeroResponseDTO>> response = heroController.getHeroesByUserId(1L);
		assertFalse(response.getBody().isEmpty());
		assertEquals("Paladin", response.getBody().get(0).getName());
	}

	@Test
	void getHeroesByUserIdNoContent() {
		when(heroService.getHeroesByUserId(anyLong())).thenReturn(Collections.emptyList());

		ResponseEntity<List<HeroResponseDTO>> response = heroController.getHeroesByUserId(1L);
		assertEquals(204, response.getStatusCode().value());
	}

	@Test
	void searchHeroesOk() {
		HeroResponseDTO hero = new HeroResponseDTO();
		hero.setId(1L);
		hero.setName("Bard");

		when(heroService.searchHeroesByNameAndHeroClass(anyString(), anyString())).thenReturn(List.of(hero));

		ResponseEntity<List<HeroResponseDTO>> response = heroController.searchHeroes("Bard", "ALL");
		assertFalse(response.getBody().isEmpty());
		assertEquals("Bard", response.getBody().get(0).getName());
	}

	@Test
	void searchHeroesNoContent() {
		when(heroService.searchHeroesByNameAndHeroClass(anyString(), anyString())).thenReturn(Collections.emptyList());

		ResponseEntity<List<HeroResponseDTO>> response = heroController.searchHeroes("Bard", "ALL");
		assertEquals(204, response.getStatusCode().value());
	}
}