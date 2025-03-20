package com.criticalblunder.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.criticalblunder.dto.request.HeroCampaignRequestDTO;
import com.criticalblunder.dto.request.HeroRequestDTO;
import com.criticalblunder.dto.request.HeroRequestUpdateDTO;
import com.criticalblunder.dto.response.HeroResponseDTO;
import com.criticalblunder.enums.HeroClassEnum;
import com.criticalblunder.model.Campaign;
import com.criticalblunder.model.Hero;
import com.criticalblunder.model.HeroCampaign;
import com.criticalblunder.model.User;
import com.criticalblunder.repository.CampaignRepository;
import com.criticalblunder.repository.HeroCampaignRepository;
import com.criticalblunder.repository.HeroRepository;
import com.criticalblunder.repository.UserRepository;
import com.criticalblunder.service.HeroService;

import jakarta.transaction.Transactional;
import mapper.HeroCampaignMapper;
import mapper.HeroMapper;

@Service
public class HeroServiceImpl implements HeroService {

	private final HeroRepository heroRepository;
	private final UserRepository userRepository;
	private final HeroMapper heroMapper;
	private final HeroCampaignRepository heroCampaignRepository;
	private final CampaignRepository campaignRepository;

	public HeroServiceImpl(HeroRepository heroRepository, UserRepository userRepository, HeroMapper heroMapper,
			HeroCampaignRepository heroCampaignRepository, CampaignRepository campaignRepository, HeroCampaignMapper heroCampaignMapper) {
		this.heroRepository = heroRepository;
		this.heroMapper = heroMapper;
		this.userRepository = userRepository;
		this.heroCampaignRepository = heroCampaignRepository;
		this.campaignRepository = campaignRepository;

	}

	@Transactional
	@Override
	public void createHero(HeroRequestDTO request, User user) {
		Hero hero = heroMapper.toEntity(request);
		hero.setUser(user);

		heroRepository.save(hero);
	}

	@Override
	public Hero findHeroByUserEmail(String email) {
		return heroRepository.findHeroByUserEmail(email)
				.orElseThrow(() -> new RuntimeException("Héroe no encontrado."));
	}

	@Transactional
	@Override
	public void deleteHero(Long heroId, User user) {
		Hero hero = heroRepository.findById(heroId).orElseThrow(() -> new RuntimeException("Héroe no encontrado."));

		if (!hero.getUser().getId().equals(user.getId())) {
			throw new SecurityException("No puedes eliminar héroes que no te pertenecen.");
		}

		boolean isInCampaign = heroCampaignRepository.existsByHeroId(heroId);
		if (isInCampaign) {
			throw new IllegalStateException("No puedes eliminar este héroe porque está asignado a una campaña.");
		}

		heroRepository.delete(hero);
	}

	@Override
	public List<Campaign> getCampaignsForHero(Long heroId) {
		return campaignRepository.findCampaignsByHeroId(heroId);
	}

	@Transactional
	@Override
	public void leaveCampaign(Long heroId, Long campaignId, User user) {
		HeroCampaign heroCampaign = heroCampaignRepository.findByHeroIdAndCampaignId(heroId, campaignId)
				.orElseThrow(() -> new RuntimeException("Héroe no está en esta campaña."));

		if (!heroCampaign.getHero().getUser().getId().equals(user.getId())) {
			throw new SecurityException("No puedes retirar un héroe que no te pertenece.");
		}

		heroCampaignRepository.delete(heroCampaign);
	}

	@Transactional
	@Override
	public void kickHeroFromCampaign(Long heroId, Long campaignId) {
		HeroCampaign heroCampaign = heroCampaignRepository.findByHeroIdAndCampaignId(heroId, campaignId)
				.orElseThrow(() -> new RuntimeException("El héroe no está en esta campaña."));

		heroCampaignRepository.delete(heroCampaign);
	}

	@Transactional
	@Override
	public void updateHero(Long heroId, HeroRequestUpdateDTO request, User user) {
		Hero hero = heroRepository.findById(heroId).orElseThrow(() -> new RuntimeException("Héroe no encontrado."));

		if (!hero.getUser().getId().equals(user.getId())) {
			throw new SecurityException("No puedes modificar un héroe que no te pertenece.");
		}

		hero.setName(request.getName() != null ? request.getName() : hero.getName());
		hero.setDescription(request.getDescription() != null ? request.getDescription() : hero.getDescription());
		hero.setAge(request.getAge() != null ? request.getAge() : hero.getAge());
		hero.setAppearance(request.getAppearance() != null ? request.getAppearance() : hero.getAppearance());

		heroRepository.save(hero);
	}

	@Transactional
	@Override
	public void updateHeroCampaign(Long heroId, Long campaignId, HeroCampaignRequestDTO request) {
		HeroCampaign heroCampaign = heroCampaignRepository.findByHeroIdAndCampaignId(heroId, campaignId)
				.orElseThrow(() -> new RuntimeException("El héroe no está asignado a esta campaña."));

		heroCampaign.setLevel(request.getLevel() != null ? request.getLevel() : heroCampaign.getLevel());
		heroCampaign.setExperience(
				request.getExperience() != null ? request.getExperience() : heroCampaign.getExperience());
		heroCampaign.setStatus(request.getStatus() != null ? request.getStatus() : heroCampaign.getStatus());

		heroCampaignRepository.save(heroCampaign);
	}

	@Override
	public void validateHeroOwnership(Long heroId, User player) {
		Hero hero = heroRepository.findById(heroId).orElseThrow(() -> new RuntimeException("Héroe no encontrado."));

		if (!hero.getUser().getId().equals(player.getId())) {
			throw new SecurityException("No puedes editar un héroe que no te pertenece.");
		}
	}

	public List<HeroResponseDTO> getUserHeroes(User user) {
		List<Hero> heroes = heroRepository.findByUser(user);
		return heroes.stream().map(heroMapper::toResponseDTO).collect(Collectors.toList());
	}

	@Override
	public HeroResponseDTO getHeroResponse(Long heroId) {
		Hero hero = heroRepository.findById(heroId).orElseThrow(() -> new RuntimeException("Héroe no encontrado."));

		HeroResponseDTO dto = new HeroResponseDTO();
		dto.setId(hero.getId());
		dto.setName(hero.getName());
		dto.setDescription(hero.getDescription());
		dto.setAge(hero.getAge());
		dto.setAppearance(hero.getAppearance());
		dto.setHeroClass(hero.getHeroClass().toString());
		dto.setName(hero.getUser().getEmail());

		return dto;
	}

	@Override
	public List<HeroResponseDTO> getHeroesByUserId(Long userId) {
		List<Hero> heroes = heroRepository.findAllByUserId(userId);
		return heroes.stream().map(hero -> heroMapper.toResponseDTO(hero)).collect(Collectors.toList());
	}


    @Override
    public List<HeroResponseDTO> searchHeroesByName(String ownerName) {
        List<User> users = userRepository.findByNameContainingIgnoreCase(ownerName);
        List<Hero> heroes = heroRepository.findByUserIn(users);
        return heroes.stream()
                     .map(heroMapper::toResponseDTO)
                     .collect(Collectors.toList());
    }

    public List<HeroResponseDTO> searchHeroesByNameAndHeroClass(String ownerName, String heroClass) {
        List<User> users = userRepository.findByNameContainingIgnoreCase(ownerName);
        List<Hero> heroes;
        if (heroClass != null && !heroClass.equalsIgnoreCase("ALL")) {
            HeroClassEnum heroClassEnum;
            try {
                heroClassEnum = HeroClassEnum.valueOf(heroClass.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("El valor para heroClass no es válido: " + heroClass);
            }
            heroes = heroRepository.findByUserInAndHeroClass(users, heroClassEnum);
        } else {
            heroes = heroRepository.findByUserIn(users);
        }
        return heroes.stream()
                     .map(heroMapper::toResponseDTO)
                     .collect(Collectors.toList());
    }

    


}
