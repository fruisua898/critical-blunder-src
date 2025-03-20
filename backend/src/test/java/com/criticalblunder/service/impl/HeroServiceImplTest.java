package com.criticalblunder.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.criticalblunder.dto.request.HeroCampaignRequestDTO;
import com.criticalblunder.dto.request.HeroRequestDTO;
import com.criticalblunder.dto.request.HeroRequestUpdateDTO;
import com.criticalblunder.dto.response.HeroResponseDTO;
import com.criticalblunder.enums.HeroClassEnum;
import com.criticalblunder.enums.HeroStatusEnum;
import com.criticalblunder.model.Campaign;
import com.criticalblunder.model.Hero;
import com.criticalblunder.model.HeroCampaign;
import com.criticalblunder.model.User;
import com.criticalblunder.repository.CampaignRepository;
import com.criticalblunder.repository.HeroCampaignRepository;
import com.criticalblunder.repository.HeroRepository;
import com.criticalblunder.repository.UserRepository;
import mapper.HeroCampaignMapper;
import mapper.HeroMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HeroServiceImplTest {

    @Mock private HeroRepository heroRepository;
    @Mock private UserRepository userRepository;
    @Mock private HeroCampaignRepository heroCampaignRepository;
    @Mock private CampaignRepository campaignRepository;
    @Mock private HeroMapper heroMapper;
    @Mock private HeroCampaignMapper heroCampaignMapper;

    @InjectMocks private HeroServiceImpl heroService;

    private User user;
    private Hero hero;
    private Campaign campaign;
    private HeroRequestDTO heroRequestDTO;
    private HeroCampaign heroCampaign;
    private HeroRequestUpdateDTO heroRequestUpdateDTO;
    private HeroCampaignRequestDTO heroCampaignRequestDTO;
    private HeroResponseDTO heroResponseDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("john@example.com");

        campaign = new Campaign();
        campaign.setId(1L);
        campaign.setGameMaster(user);

        hero = new Hero();
        hero.setId(1L);
        hero.setUser(user);
        hero.setName("Hero");

        heroCampaign = new HeroCampaign();
        heroCampaign.setHero(hero);
        heroCampaign.setCampaign(campaign);
        heroRequestDTO = new HeroRequestDTO("New Hero", HeroClassEnum.FIGHTER, "Description", 25, "Appearance");
        heroRequestUpdateDTO = new HeroRequestUpdateDTO("Updated Hero", "Updated Description", 30, "Updated Appearance");
        heroCampaignRequestDTO = new HeroCampaignRequestDTO(10, 500, HeroStatusEnum.ALIVE);

        heroResponseDTO = new HeroResponseDTO();
        heroResponseDTO.setId(1L);
        heroResponseDTO.setName("HeroName");
        heroResponseDTO.setDescription("Hero Desc");
    }

    @Test
    void updateHeroCampaign() {
        when(heroCampaignRepository.findByHeroIdAndCampaignId(1L, 1L)).thenReturn(Optional.of(heroCampaign));

        heroService.updateHeroCampaign(1L, 1L, heroCampaignRequestDTO);

        assertEquals(10, heroCampaign.getLevel());
        assertEquals(500, heroCampaign.getExperience());
        assertEquals(HeroStatusEnum.ALIVE, heroCampaign.getStatus());
        verify(heroCampaignRepository).save(heroCampaign);
    }


    @Test
    void searchHeroesByNameAndHeroClass() {
        when(userRepository.findByNameContainingIgnoreCase("John")).thenReturn(List.of(user));
        when(heroRepository.findByUserInAndHeroClass(List.of(user), HeroClassEnum.FIGHTER)).thenReturn(List.of(hero));
        when(heroMapper.toResponseDTO(hero)).thenReturn(heroResponseDTO);

        List<HeroResponseDTO> result = heroService.searchHeroesByNameAndHeroClass("John", "FIGHTER");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("HeroName", result.get(0).getName());
    }

    @Test
    void kickHeroFromCampaign() {
        when(heroCampaignRepository.findByHeroIdAndCampaignId(1L, 1L)).thenReturn(Optional.of(heroCampaign));

        heroService.kickHeroFromCampaign(1L, 1L);

        verify(heroCampaignRepository).delete(heroCampaign);
    }

    @Test
    void getHeroesByUserId() {
        when(heroRepository.findAllByUserId(1L)).thenReturn(List.of(hero));
        when(heroMapper.toResponseDTO(hero)).thenReturn(heroResponseDTO);

        List<HeroResponseDTO> result = heroService.getHeroesByUserId(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void updateHero() {
        when(heroRepository.findById(1L)).thenReturn(Optional.of(hero));
        when(heroRepository.save(any(Hero.class))).thenReturn(hero);

        heroService.updateHero(1L, heroRequestUpdateDTO, user);

        assertEquals("Updated Hero", hero.getName());
        assertEquals("Updated Description", hero.getDescription());
        assertEquals(30, hero.getAge());
        assertEquals("Updated Appearance", hero.getAppearance());
        verify(heroRepository).save(hero);
    }

    @Test
    void getCampaignsForHero() {
        when(campaignRepository.findCampaignsByHeroId(1L)).thenReturn(List.of(campaign));

        List<Campaign> result = heroService.getCampaignsForHero(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
    



    @Test
    void shouldThrowExceptionWhenHeroNotFoundInGetHeroResponse() {
        when(heroRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> heroService.getHeroResponse(1L));
    }

    @Test
    void shouldDeleteHeroSuccessfully() {
        when(heroRepository.findById(1L)).thenReturn(Optional.of(hero));
        when(heroCampaignRepository.existsByHeroId(1L)).thenReturn(false);

        heroService.deleteHero(1L, user);

        verify(heroRepository).delete(hero);
    }

    @Test
    void shouldThrowExceptionWhenDeletingHeroNotOwned() {
        User anotherUser = new User(2L, "Other User", "other@example.com", "password", null);
        hero.setUser(anotherUser);

        when(heroRepository.findById(1L)).thenReturn(Optional.of(hero));

        assertThrows(SecurityException.class, () -> heroService.deleteHero(1L, user));
    }

    @Test
    void shouldThrowExceptionWhenDeletingHeroInCampaign() {
        when(heroRepository.findById(1L)).thenReturn(Optional.of(hero));
        when(heroCampaignRepository.existsByHeroId(1L)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> heroService.deleteHero(1L, user));
    }

    @Test
    void shouldLeaveCampaignSuccessfully() {
        when(heroCampaignRepository.findByHeroIdAndCampaignId(1L, 1L)).thenReturn(Optional.of(heroCampaign));

        heroService.leaveCampaign(1L, 1L, user);

        verify(heroCampaignRepository).delete(heroCampaign);
    }

    @Test
    void shouldThrowExceptionWhenLeavingCampaignNotOwned() {
        User anotherUser = new User(2L, "Other User", "other@example.com", "password", null);
        hero.setUser(anotherUser);

        when(heroCampaignRepository.findByHeroIdAndCampaignId(1L, 1L)).thenReturn(Optional.of(heroCampaign));

        assertThrows(SecurityException.class, () -> heroService.leaveCampaign(1L, 1L, user));
    }

    @Test
    void shouldSearchHeroesByName() {
        when(userRepository.findByNameContainingIgnoreCase("John")).thenReturn(List.of(user));
        when(heroRepository.findByUserIn(List.of(user))).thenReturn(List.of(hero));
        when(heroMapper.toResponseDTO(hero)).thenReturn(heroResponseDTO);

        List<HeroResponseDTO> heroes = heroService.searchHeroesByName("John");

        assertFalse(heroes.isEmpty());
        assertEquals("HeroName", heroes.get(0).getName());
    }

    @Test
    void shouldValidateHeroOwnershipSuccessfully() {
        when(heroRepository.findById(1L)).thenReturn(Optional.of(hero));

        assertDoesNotThrow(() -> heroService.validateHeroOwnership(1L, user));
    }

    @Test
    void shouldThrowExceptionWhenValidatingHeroOwnershipNotOwned() {
        User anotherUser = new User(2L, "Other User", "other@example.com", "password", null);
        hero.setUser(anotherUser);

        when(heroRepository.findById(1L)).thenReturn(Optional.of(hero));

        assertThrows(SecurityException.class, () -> heroService.validateHeroOwnership(1L, user));
    }

    @Test
    void shouldGetUserHeroes() {
        when(heroRepository.findByUser(user)).thenReturn(List.of(hero));
        when(heroMapper.toResponseDTO(hero)).thenReturn(heroResponseDTO);

        List<HeroResponseDTO> heroes = heroService.getUserHeroes(user);

        assertFalse(heroes.isEmpty());
        assertEquals(1, heroes.size());
        assertEquals("HeroName", heroes.get(0).getName());
    }

    @Test
    void shouldCreateHeroSuccessfully() {
        when(heroMapper.toEntity(heroRequestDTO)).thenReturn(hero);
        when(heroRepository.save(hero)).thenReturn(hero);

        heroService.createHero(heroRequestDTO, user);

        verify(heroRepository).save(hero);
    }
}