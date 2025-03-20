package com.criticalblunder.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.criticalblunder.dto.request.CampaignRequestDTO;
import com.criticalblunder.dto.request.HeroCampaignRequestDTO;
import com.criticalblunder.dto.response.CampaignResponseDTO;
import com.criticalblunder.enums.CampaignStatusEnum;
import com.criticalblunder.exception.CampaignNotFoundException;
import com.criticalblunder.model.Campaign;
import com.criticalblunder.model.User;
import com.criticalblunder.service.CampaignService;
import com.criticalblunder.service.HeroService;
import com.criticalblunder.service.MessageService;
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
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CampaignControllerTest {

	@Mock
	private CampaignService campaignService;

	@Mock
	private MessageService messageService;

	@Mock
	private UserService userService;

	@Mock
	private HeroService heroService;

	@InjectMocks
	private CampaignController campaignController;

	private User user;
	private CustomUserDetails userDetails;

	@BeforeEach
	void setUp() {
		user = new User();
		user.setId(1L);
		user.setEmail("testUser@testuser");
		userDetails = new CustomUserDetails(user);
	}

	@Test
	void createCampaignOk() {
		CampaignRequestDTO requestDTO = new CampaignRequestDTO();
		requestDTO.setName("New Campaign");
		requestDTO.setDescription("Campaign Description");

		Campaign campaign = new Campaign();
		campaign.setId(1L);
		campaign.setName("New Campaign");

		when(userService.getUserOrThrow(userDetails.getUsername())).thenReturn(user);
		when(campaignService.getUserActiveCampaigns(user.getId())).thenReturn(Collections.emptyList());
		when(campaignService.createCampaign(anyString(), anyString(), any(User.class))).thenReturn(campaign);

		ResponseEntity<?> response = campaignController.createCampaign(userDetails, requestDTO);
		assertEquals(200, response.getStatusCode().value());
	}

	@Test
	void listAllCampaignsNoContent() {
		when(campaignService.getAllCampaigns()).thenReturn(Collections.emptyList());

		ResponseEntity<List<CampaignResponseDTO>> response = campaignController.listAllCampaigns();
		assertEquals(204, response.getStatusCode().value());
	}

	@Test
	void findCampaignByIdNotFound() {
		when(campaignService.findById(anyLong())).thenReturn(Optional.empty());
		when(messageService.getMessage("campaign.find.notfound")).thenReturn("Campaign not found");

		Exception exception = assertThrows(CampaignNotFoundException.class,
				() -> campaignController.findCampaignById(1L));

		assertEquals("Campaign not found", exception.getMessage());
	}

	@Test
	void deleteCampaignOk() {
		when(userService.getUserOrThrow(userDetails.getUsername())).thenReturn(user);
		doNothing().when(campaignService).deleteCampaign(anyLong(), any(User.class));
		when(messageService.getMessage("campaign.delete.ok")).thenReturn("Campaign deleted");

		ResponseEntity<?> response = campaignController.deleteCampaign(1L, userDetails);
		assertEquals(200, response.getStatusCode().value());
		assertEquals("Campaign deleted", response.getBody());
	}

	@Test
	void updateCampaignStatusOk() {
		when(userService.getUserOrThrow(userDetails.getUsername())).thenReturn(user);
		doNothing().when(campaignService).updateCampaignStatus(anyLong(), any(CampaignStatusEnum.class),
				any(User.class));
		when(messageService.getMessage("campaign.update.state.ok")).thenReturn("Campaign status updated to ");

		ResponseEntity<?> response = campaignController.updateCampaignStatus(1L, CampaignStatusEnum.ACTIVE,
				userDetails);
		assertEquals(200, response.getStatusCode().value());
		assertEquals("Campaign status updated to ACTIVE", response.getBody());
	}

	@Test
	void assignHeroToCampaignOk() {
		when(userService.getUserOrThrow(userDetails.getUsername())).thenReturn(user);
		doNothing().when(campaignService).validateGameMaster(user, 1L);
		doNothing().when(campaignService).addHeroToCampaign(2L, 1L, user);

		ResponseEntity<?> response = campaignController.assignHeroToCampaign(1L, 2L, userDetails);
		assertEquals(200, response.getStatusCode().value());
		assertEquals("Héroe asignado correctamente.", response.getBody());
	}

	@Test
	void removeHeroFromCampaignOk() {
		when(userService.getUserOrThrow(userDetails.getUsername())).thenReturn(user);
		doNothing().when(campaignService).validateGameMaster(user, 1L);
		doNothing().when(heroService).kickHeroFromCampaign(2L, 1L);

		ResponseEntity<?> response = campaignController.removeHeroFromCampaign(1L, 2L, userDetails);
		assertEquals(200, response.getStatusCode().value());
		assertEquals("Héroe expulsado de la campaña.", response.getBody());
	}

	@Test
	void updateHeroInCampaignOk() {
		HeroCampaignRequestDTO requestDTO = new HeroCampaignRequestDTO();
		when(userService.getUserOrThrow(userDetails.getUsername())).thenReturn(user);
		doNothing().when(campaignService).validateGameMaster(user, 1L);
		doNothing().when(heroService).updateHeroCampaign(2L, 1L, requestDTO);

		ResponseEntity<?> response = campaignController.updateHeroInCampaign(1L, 2L, requestDTO, userDetails);
		assertEquals(200, response.getStatusCode().value());
		assertEquals("Héroe actualizado correctamente en la campaña.", response.getBody());
	}
}