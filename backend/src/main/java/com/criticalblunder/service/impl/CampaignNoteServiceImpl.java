package com.criticalblunder.service.impl;

import com.criticalblunder.dto.request.CampaignNoteRequestDTO;
import com.criticalblunder.exception.CampaignNotFoundException;
import com.criticalblunder.exception.NoteNotFoundException;
import com.criticalblunder.exception.NotePermissionException;
import com.criticalblunder.exception.UserNotFoundException;
import com.criticalblunder.model.Campaign;
import com.criticalblunder.model.CampaignNote;
import com.criticalblunder.model.User;
import com.criticalblunder.repository.CampaignNoteRepository;
import com.criticalblunder.repository.CampaignRepository;
import com.criticalblunder.repository.HeroCampaignRepository;
import com.criticalblunder.repository.HeroRepository;
import com.criticalblunder.repository.UserRepository;
import com.criticalblunder.service.CampaignNoteService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CampaignNoteServiceImpl implements CampaignNoteService {

	private final CampaignNoteRepository campaignNoteRepository;
	private final CampaignRepository campaignRepository;
	private final UserRepository userRepository;
	private final HeroRepository heroRepository;

	public CampaignNoteServiceImpl(CampaignNoteRepository campaignNoteRepository, CampaignRepository campaignRepository,
			HeroRepository heroRepository, UserRepository userRepository) {
		this.campaignNoteRepository = campaignNoteRepository;
		this.campaignRepository = campaignRepository;
		this.userRepository = userRepository;
		this.heroRepository = heroRepository;
	}

	@Transactional
	@Override
	public CampaignNote createNote(Long campaignId, CampaignNoteRequestDTO request, String userEmail) {
		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new UserNotFoundException("Usuario no encontrado."));

		if (!hasPermission(campaignId, user)) {
			throw new NotePermissionException("No tienes permisos para crear notas en esta campaña.");
		}

		Campaign campaign = campaignRepository.findById(campaignId)
				.orElseThrow(() -> new CampaignNotFoundException("Campaña no encontrada."));

		CampaignNote note = new CampaignNote();
		note.setTitle(request.getTitle());
		note.setContent(request.getContent());
		note.setAuthor(user.getName());
		note.setCampaign(campaign);

		return campaignNoteRepository.save(note);
	}

	@Override
	public List<CampaignNote> getNotesByCampaign(Long campaignId) {
		return campaignNoteRepository.findByCampaignId(campaignId);
	}

	@Transactional
	@Override
	public CampaignNote updateNote(Long noteId, CampaignNoteRequestDTO request, String userEmail) {
		CampaignNote note = campaignNoteRepository.findById(noteId)
				.orElseThrow(() -> new NoteNotFoundException("Nota no encontrada."));

		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new UserNotFoundException("Usuario no encontrado."));

		boolean isAuthor = note.getAuthor().equals(user.getName());
		boolean isGameMaster = note.getCampaign().getGameMaster().getId().equals(user.getId());

		if (!isAuthor && !isGameMaster) {
			throw new NotePermissionException("No tienes permisos para modificar esta nota.");
		}

		note.setTitle(request.getTitle());
		note.setContent(request.getContent());

		return campaignNoteRepository.save(note);
	}

	@Transactional
	@Override
	public void deleteNote(Long noteId, String userEmail) {
		CampaignNote note = campaignNoteRepository.findById(noteId)
				.orElseThrow(() -> new NoteNotFoundException("Nota no encontrada."));

		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new UserNotFoundException("Usuario no encontrado."));

		boolean isAuthor = note.getAuthor().equals(user.getName());
		boolean isGameMaster = note.getCampaign().getGameMaster().getId().equals(user.getId());

		if (!isAuthor && !isGameMaster) {
			throw new NotePermissionException("No tienes permisos para eliminar esta nota.");
		}

		campaignNoteRepository.delete(note);
	}

	private boolean hasPermission(Long campaignId, User user) {
		Campaign campaign = campaignRepository.findById(campaignId)
				.orElseThrow(() -> new CampaignNotFoundException("Campaña con ID " + campaignId + " no encontrada."));

		boolean isGameMaster = campaign.getGameMaster().getId().equals(user.getId());

		boolean hasHeroInCampaign = heroRepository.existsHeroInCampaignByUserId(user.getId(), campaignId);

		return isGameMaster || hasHeroInCampaign;
	}

}
