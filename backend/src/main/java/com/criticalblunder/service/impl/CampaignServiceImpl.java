package com.criticalblunder.service.impl;

import com.criticalblunder.service.CampaignService;
import com.criticalblunder.service.MessageService;
import com.criticalblunder.service.UserService;

import jakarta.transaction.Transactional;
import mapper.CampaignMapper;
import mapper.HeroCampaignMapper;
import mapper.HeroMapper;

import com.criticalblunder.constants.RoleConstants;
import com.criticalblunder.dto.request.CampaignRequestDTO;
import com.criticalblunder.dto.response.CampaignResponseDTO;
import com.criticalblunder.dto.response.HeroCampaignResponseDTO;
import com.criticalblunder.dto.response.HeroResponseDTO;
import com.criticalblunder.enums.CampaignStatusEnum;
import com.criticalblunder.enums.HeroStatusEnum;
import com.criticalblunder.exception.CampaignNotFoundException;
import com.criticalblunder.exception.DuplicateCampaignNameException;
import com.criticalblunder.constants.CampaignConstants;
import com.criticalblunder.model.Campaign;
import com.criticalblunder.model.Hero;
import com.criticalblunder.model.HeroCampaign;
import com.criticalblunder.model.HeroCampaignId;
import com.criticalblunder.model.User;
import com.criticalblunder.repository.CampaignRepository;
import com.criticalblunder.repository.HeroCampaignRepository;
import com.criticalblunder.repository.HeroRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CampaignServiceImpl implements CampaignService {

	private final CampaignRepository campaignRepository;
	private final UserService userService;
	private final CampaignMapper campaignMapper;
	private final MessageService messageService;
	private final HeroCampaignRepository heroCampaignRepository;
	private final HeroRepository heroRepository;
	private final HeroMapper heroMapper;
	private final HeroCampaignMapper heroCampaignMapper;

	public CampaignServiceImpl(CampaignRepository campaignRepository, UserService userService,
			CampaignMapper campaignMapper, MessageService messageService, HeroCampaignRepository heroCampaignRepository,
			HeroRepository heroRepository, HeroMapper heroMapper, HeroCampaignMapper heroCampaignMapper) {
		this.campaignRepository = campaignRepository;
		this.userService = userService;
		this.campaignMapper = campaignMapper;
		this.messageService = messageService;
		this.heroCampaignRepository = heroCampaignRepository;
		this.heroRepository = heroRepository;
		this.heroMapper = heroMapper;
		this.heroCampaignMapper = heroCampaignMapper;
	}

	@Override
	public Campaign createCampaign(String title, String description, User gameMaster) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		gameMaster = userService.getUserOrThrow(userDetails.getUsername());

		Campaign campaign = this.setCampaignDetails(title, description, gameMaster);

		if (campaign.getName().equals(title) && campaignRepository.existsByNameAndGameMaster(title, gameMaster)) {
			throw new DuplicateCampaignNameException(
					"Tienes una campaña con el nombre '" + title + "'. Elige otro nombre.");
		}
		userService.updateUserRole(gameMaster, true);
		return campaignRepository.save(campaign);
	}

	@Override
	public List<CampaignResponseDTO> getUserCampaigns(Long gameMasterId) {
		List<Campaign> campaigns = campaignRepository.findByGameMasterId(gameMasterId);
		return campaigns.stream().map(campaignMapper::toResponseDTO).collect(Collectors.toList());
	}

	public List<CampaignResponseDTO> getUserActiveCampaigns(Long gameMasterId) {
		List<CampaignResponseDTO> campaigns = this.getUserCampaigns(gameMasterId).stream()
				.filter(campaign -> CampaignStatusEnum.ACTIVE.equals(campaign.getStatus()))
				.collect(Collectors.toList());
		return campaigns;
	}

	@Override
	public Optional<CampaignResponseDTO> findByName(String name) {

		Optional<Campaign> campaign = campaignRepository.findByName(name);
		return campaign.map(campaignMapper::toResponseDTO);
	}

	public List<CampaignResponseDTO> getAllCampaigns() {
		List<Campaign> campaigns = campaignRepository.findAll();
		return campaigns.stream().map(campaignMapper::toResponseDTO).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void deleteCampaign(Long campaignId, User user) {
		Campaign campaign = campaignRepository.findById(campaignId)
				.orElseThrow(() -> new RuntimeException("Campaign no encontrada"));

		if (!campaign.getGameMaster().equals(user)) {
			throw new SecurityException("No tienes permisos para eliminar esta campaña.");
		}

		campaignRepository.deleteById(campaignId);

		User gameMaster = campaign.getGameMaster();
		List<Campaign> remainingCampaigns = campaignRepository.findByGameMasterId(gameMaster.getId());

		if (remainingCampaigns.isEmpty() && gameMaster.getRole() != RoleConstants.SUPER_USER) {
			userService.updateUserRole(gameMaster, false);
		}
	}

	@Override
	@Transactional
	public void updateCampaignStatus(Long campaignId, CampaignStatusEnum newStatus, User user) {
		Campaign campaign = campaignRepository.findById(campaignId)
				.orElseThrow(() -> new CampaignNotFoundException(messageService.getMessage("campaign.find.notfound")));

		if (getUserActiveCampaigns(user.getId()).size() == 3 && newStatus == CampaignStatusEnum.ACTIVE) {
			throw new IllegalStateException(messageService.getMessage("campaign.active.max.allowed"));
		}
		if (!campaign.getGameMaster().equals(user)) {
			throw new SecurityException("No tienes permisos para modificar esta campaña.");
		}

		campaign.setStatus(newStatus);
		campaignRepository.save(campaign);
	}

	@Override
	@Transactional
	public Campaign updateCampaignDetails(Long id, CampaignRequestDTO request, User authenticatedUser) {
		Campaign campaign = campaignRepository.findById(id)
				.orElseThrow(() -> new CampaignNotFoundException(messageService.getMessage("campaign.find.notfound")));

		if (!campaign.getGameMaster().equals(authenticatedUser)) {
			throw new IllegalStateException(messageService.getMessage("campaign.update.forbidden"));
		}

		campaign.setName(request.getName());
		campaign.setDescription(request.getDescription());
		return campaignRepository.save(campaign);
	}

	private Campaign setCampaignDetails(String title, String description, User user) {

		Date actualDate = new Date();
		Campaign campaign = new Campaign();
		campaign.setName(title);
		campaign.setCreatedAt(actualDate);
		campaign.setStatus(CampaignConstants.DEFAULT);
		campaign.setDescription(description);
		campaign.setGameMaster(user);
		return campaign;

	}

	@Override
	public Optional<CampaignResponseDTO> findById(Long id) {

		Optional<Campaign> campaign = campaignRepository.findById(id);
		return campaign.map(campaignMapper::toResponseDTO);
	}

	@Override
	@Transactional
	public void addHeroToCampaign(Long heroId, Long campaignId, User user) {
		Hero hero = heroRepository.findById(heroId).orElseThrow(() -> new RuntimeException("Héroe no encontrado."));
		Campaign campaign = campaignRepository.findById(campaignId)
				.orElseThrow(() -> new RuntimeException("Campaña no encontrada."));

		if (!campaign.getGameMaster().getId().equals(user.getId())) {
			throw new SecurityException("No tienes permisos para asignar héroes en esta campaña.");
		}

		boolean hasHeroInCampaign = heroCampaignRepository.existsByHeroIdAndCampaignId(heroId, campaignId);
		if (hasHeroInCampaign) {
			throw new IllegalStateException("Este héroe ya está en la campaña.");
		}

		HeroCampaignId heroCampaignId = new HeroCampaignId(heroId, campaignId);
		HeroCampaign heroCampaign = new HeroCampaign();
		heroCampaign.setId(heroCampaignId);
		heroCampaign.setHero(hero);
		heroCampaign.setCampaign(campaign);
		heroCampaign.setLevel(1);
		heroCampaign.setStatus(HeroStatusEnum.ALIVE);

		heroCampaignRepository.save(heroCampaign);
	}

	@Override
	public void validateGameMaster(User user, Long campaignId) {
		Campaign campaign = campaignRepository.findById(campaignId)
				.orElseThrow(() -> new RuntimeException("Campaña no encontrada."));

		if (!campaign.getGameMaster().getId().equals(user.getId())) {
			throw new SecurityException("No tienes permisos para modificar esta campaña.");
		}
	}

	@Override
	public List<CampaignResponseDTO> getCampaignsWhereUserIsParticipant(Long userId) {
		List<Hero> userHeroes = heroRepository.findAllByUserId(userId);
		Set<Campaign> participantCampaigns = new HashSet<>();

		for (Hero hero : userHeroes) {
			List<Campaign> campaigns = campaignRepository.findCampaignsByHeroId(hero.getId());
			participantCampaigns.addAll(campaigns);
		}

		return participantCampaigns.stream().map(campaignMapper::toResponseDTO).collect(Collectors.toList());
	}

	@Override
	public List<HeroCampaignResponseDTO> getAssignedHeroes(Long campaignId, User gm) {
		Campaign campaign = campaignRepository.findById(campaignId)
				.orElseThrow(() -> new CampaignNotFoundException("Campaña no encontrada"));

		if (!campaign.getGameMaster().getId().equals(gm.getId())) {
			throw new IllegalStateException("No tienes permisos para ver los héroes asignados a esta campaña");
		}

		List<HeroCampaign> heroCampaigns = heroCampaignRepository.findByCampaignId(campaignId);

		return heroCampaigns.stream().map(heroCampaign -> heroCampaignMapper.toResponseDTO(heroCampaign))
				.collect(Collectors.toList());
	}
}
