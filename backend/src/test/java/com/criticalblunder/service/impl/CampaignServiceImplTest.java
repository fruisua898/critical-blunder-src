package com.criticalblunder.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.criticalblunder.dto.request.CampaignRequestDTO;
import com.criticalblunder.dto.response.CampaignResponseDTO;
import com.criticalblunder.dto.response.HeroCampaignResponseDTO;
import com.criticalblunder.enums.CampaignStatusEnum;
import com.criticalblunder.model.Campaign;
import com.criticalblunder.model.Hero;
import com.criticalblunder.model.HeroCampaign;
import com.criticalblunder.model.User;
import com.criticalblunder.repository.CampaignRepository;
import com.criticalblunder.repository.HeroCampaignRepository;
import com.criticalblunder.repository.HeroRepository;
import com.criticalblunder.service.MessageService;
import com.criticalblunder.service.UserService;
import mapper.CampaignMapper;
import mapper.HeroCampaignMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CampaignServiceImplTest {

	@Mock
	private CampaignRepository campaignRepository;

	@Mock
	private UserService userService;

	@Mock
	private CampaignMapper campaignMapper;

	@Mock
	private MessageService messageService;

	@Mock
	private SecurityContext securityContext;

	@Mock
	private Authentication authentication;

	@Mock
	private UserDetails userDetails;

	@Mock
	private HeroCampaignRepository heroCampaignRepository;

    @Mock
    private HeroRepository heroRepository;

    @Mock
    private HeroCampaignMapper heroCampaignMapper;

	@InjectMocks
	private CampaignServiceImpl campaignService;

	private User gameMaster;
	private Campaign campaign;

	@BeforeEach
	void setUp() {
		gameMaster = new User();
		gameMaster.setId(1L);

		campaign = new Campaign();
		campaign.setId(1L);
		campaign.setName("Test Campaign");
		campaign.setGameMaster(gameMaster);
	}

	@Test
	void createCampaignOk() {
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(authentication.getPrincipal()).thenReturn(userDetails);
		when(userDetails.getUsername()).thenReturn("gmUser");
		SecurityContextHolder.setContext(securityContext);

		when(campaignRepository.existsByNameAndGameMaster(anyString(), any(User.class))).thenReturn(false);
		when(userService.getUserOrThrow(anyString())).thenReturn(gameMaster);
		when(campaignRepository.save(any(Campaign.class))).thenReturn(campaign);

		Campaign createdCampaign = campaignService.createCampaign("Test Campaign", "Description", gameMaster);

		assertNotNull(createdCampaign);
		assertEquals("Test Campaign", createdCampaign.getName());
		verify(userService).updateUserRole(gameMaster, true);
	}

	@Test
	void createCampaignDuplicateNameError() {

		assertThrows(NullPointerException.class,
				() -> campaignService.createCampaign("Test Campaign", "Description", gameMaster));
	}

	@Test
	void getUserCampaignsOk() {
		when(campaignRepository.findByGameMasterId(anyLong())).thenReturn(List.of(campaign));
		when(campaignMapper.toResponseDTO(any(Campaign.class))).thenReturn(new CampaignResponseDTO());

		List<CampaignResponseDTO> campaigns = campaignService.getUserCampaigns(1L);

		assertFalse(campaigns.isEmpty());
		verify(campaignRepository).findByGameMasterId(1L);
	}

	@Test
	void findByNameOk() {
		when(campaignRepository.findByName(anyString())).thenReturn(Optional.of(campaign));
		when(campaignMapper.toResponseDTO(any(Campaign.class))).thenReturn(new CampaignResponseDTO());

		Optional<CampaignResponseDTO> result = campaignService.findByName("Test Campaign");

		assertTrue(result.isPresent());
	}

	@Test
	void findByNameNotFound() {
		when(campaignRepository.findByName(anyString())).thenReturn(Optional.empty());

		Optional<CampaignResponseDTO> result = campaignService.findByName("Nonexistent Campaign");

		assertFalse(result.isPresent());
	}

	@Test
	void updateCampaignStatusOk() {
		when(campaignRepository.findById(anyLong())).thenReturn(Optional.of(campaign));
		when(campaignRepository.save(any(Campaign.class))).thenReturn(campaign);

		campaignService.updateCampaignStatus(1L, CampaignStatusEnum.ACTIVE, gameMaster);

		assertEquals(CampaignStatusEnum.ACTIVE, campaign.getStatus());
		verify(campaignRepository).save(campaign);
	}

	@Test
	void updateCampaignStatusUnauthorizedError() {
		User otherUser = new User();
		otherUser.setId(2L);
		when(campaignRepository.findById(anyLong())).thenReturn(Optional.of(campaign));

		assertThrows(SecurityException.class,
				() -> campaignService.updateCampaignStatus(1L, CampaignStatusEnum.ACTIVE, otherUser));
	}

	@Test
	void deleteCampaignOk() {
		when(campaignRepository.findById(anyLong())).thenReturn(Optional.of(campaign));
		when(campaignRepository.findByGameMasterId(anyLong())).thenReturn(List.of());

		campaignService.deleteCampaign(1L, gameMaster);

		verify(campaignRepository).deleteById(1L);
		verify(userService).updateUserRole(gameMaster, false);
	}

	@Test
	void deleteCampaignUnauthorizedError() {
		User otherUser = new User();
		otherUser.setId(2L);
		when(campaignRepository.findById(anyLong())).thenReturn(Optional.of(campaign));

		assertThrows(SecurityException.class, () -> campaignService.deleteCampaign(1L, otherUser));
	}


    @Test
    void addHeroToCampaignSuccess() {
        Long heroId = 1L;
        Long campaignId = 1L;
        Hero hero = new Hero();
        hero.setId(heroId);

        when(heroRepository.findById(heroId)).thenReturn(Optional.of(hero));
        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));
        when(heroCampaignRepository.existsByHeroIdAndCampaignId(heroId, campaignId)).thenReturn(false);

        assertDoesNotThrow(() -> campaignService.addHeroToCampaign(heroId, campaignId, gameMaster));
        verify(heroCampaignRepository).save(any(HeroCampaign.class));
    }

    @Test
    void addHeroToCampaignAlreadyExists() {
        Long heroId = 1L;
        Long campaignId = 1L;

        when(heroRepository.findById(heroId)).thenReturn(Optional.of(new Hero()));
        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));
        when(heroCampaignRepository.existsByHeroIdAndCampaignId(heroId, campaignId)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> campaignService.addHeroToCampaign(heroId, campaignId, gameMaster));
    }

    @Test
    void getCampaignsWhereUserIsParticipantSuccess() {
        Long userId = 1L;
        Hero hero = new Hero();
        hero.setId(1L);

        when(heroRepository.findAllByUserId(userId)).thenReturn(List.of(hero));
        when(campaignRepository.findCampaignsByHeroId(hero.getId())).thenReturn(List.of(campaign));
        when(campaignMapper.toResponseDTO(any(Campaign.class))).thenReturn(new CampaignResponseDTO());

        List<CampaignResponseDTO> result = campaignService.getCampaignsWhereUserIsParticipant(userId);

        assertEquals(1, result.size());
    }

    @Test
    void updateCampaignDetailsSuccess() {
        Long campaignId = 1L;
        CampaignRequestDTO request = new CampaignRequestDTO();
        request.setName("Updated Name");
        request.setDescription("Updated Description");

        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));
        when(campaignRepository.save(any(Campaign.class))).thenReturn(campaign);

        Campaign updatedCampaign = campaignService.updateCampaignDetails(campaignId, request, gameMaster);

        assertEquals("Updated Name", updatedCampaign.getName());
        assertEquals("Updated Description", updatedCampaign.getDescription());
    }

    @Test
    void getAssignedHeroesSuccess() {
        Long campaignId = 1L;
        HeroCampaign heroCampaign = new HeroCampaign();

        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));
        when(heroCampaignRepository.findByCampaignId(campaignId)).thenReturn(List.of(heroCampaign));
        when(heroCampaignMapper.toResponseDTO(heroCampaign)).thenReturn(new HeroCampaignResponseDTO());

        List<HeroCampaignResponseDTO> result = campaignService.getAssignedHeroes(campaignId, gameMaster);

        assertEquals(1, result.size());
    }

    @Test
    void validateGameMasterSuccess() {
        Long campaignId = 1L;

        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));

        assertDoesNotThrow(() -> campaignService.validateGameMaster(gameMaster, campaignId));
    }

    @Test
    void getAllCampaignsSuccess() {
        when(campaignRepository.findAll()).thenReturn(List.of(campaign));
        when(campaignMapper.toResponseDTO(any(Campaign.class))).thenReturn(new CampaignResponseDTO());

        List<CampaignResponseDTO> result = campaignService.getAllCampaigns();

        assertEquals(1, result.size());
    }

    @Test
    void findByIdSuccess() {
        Long campaignId = 1L;

        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));
        when(campaignMapper.toResponseDTO(any(Campaign.class))).thenReturn(new CampaignResponseDTO());

        Optional<CampaignResponseDTO> result = campaignService.findById(campaignId);

        assertTrue(result.isPresent());
    }

    @Test
    void getUserActiveCampaignsSuccess() {
        when(campaignRepository.findByGameMasterId(gameMaster.getId())).thenReturn(List.of(campaign));
        when(campaignMapper.toResponseDTO(any(Campaign.class))).thenReturn(new CampaignResponseDTO());

        List<CampaignResponseDTO> result = campaignService.getUserActiveCampaigns(gameMaster.getId());

        assertEquals(0, result.size());
    }

    @Test
    void updateCampaignStatusSuccess() {
        Long campaignId = 1L;
        CampaignStatusEnum newStatus = CampaignStatusEnum.ACTIVE;

        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));

        assertDoesNotThrow(() -> campaignService.updateCampaignStatus(campaignId, newStatus, gameMaster));
        verify(campaignRepository).save(any(Campaign.class));
    }



}
