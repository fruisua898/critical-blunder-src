package com.criticalblunder.controller;

import com.criticalblunder.dto.request.CampaignRequestDTO;
import com.criticalblunder.dto.request.HeroCampaignRequestDTO;
import com.criticalblunder.dto.response.CampaignResponseDTO;
import com.criticalblunder.dto.response.HeroCampaignResponseDTO;
import com.criticalblunder.dto.response.HeroResponseDTO;
import com.criticalblunder.enums.CampaignStatusEnum;
import com.criticalblunder.exception.CampaignNotFoundException;
import com.criticalblunder.model.Campaign;
import com.criticalblunder.model.User;
import com.criticalblunder.service.CampaignService;
import com.criticalblunder.service.HeroService;
import com.criticalblunder.service.MessageService;
import com.criticalblunder.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;

import jakarta.validation.Valid;
import security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

	private final CampaignService campaignService;
	private final MessageService messageService;
	private final HeroService heroService;
	private final UserService userService;

	public CampaignController(CampaignService campaignService, MessageService messageService, HeroService heroService,
			UserService userService) {
		this.campaignService = campaignService;
		this.messageService = messageService;
		this.heroService = heroService;
		this.userService = userService;
	}

	@Operation(summary = "Crear nueva campaña", description = "Crea una nueva campaña para el usuario autenticado. "
			+ "Un usuario puede tener hasta 3 campañas activas.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Campaña creada correctamente", content = @Content(schema = @Schema(implementation = Campaign.class))),
			@ApiResponse(responseCode = "400", description = "Límite de campañas alcanzado o solicitud inválida"),
			@ApiResponse(responseCode = "403", description = "Ya existe una campaña con el mismo nombre"),
			@ApiResponse(responseCode = "404", description = "Usuario no encontrado") })
	@PostMapping("/create")
	public ResponseEntity<?> createCampaign(@AuthenticationPrincipal CustomUserDetails userDetails,
			@RequestBody @Valid CampaignRequestDTO request) {
		User user = userService.getUserOrThrow(userDetails.getUsername());

		if (campaignService.getUserActiveCampaigns(user.getId()).size() >= 3) {
			throw new IllegalStateException(messageService.getMessage("campaign.active.max.allowed"));
		}

		Campaign campaign = campaignService.createCampaign(request.getName(), request.getDescription(), user);
		return ResponseEntity.ok(campaign);
	}

	@Operation(summary = "Listar todas las campañas", description = "Devuelve una lista con todas las campañas registradas. "
			+ "Si no hay campañas, se devuelve 204 (No Content).")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de campañas obtenida correctamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Campaign.class)))),
			@ApiResponse(responseCode = "204", description = "No hay campañas disponibles") })
	@GetMapping
	public ResponseEntity<List<CampaignResponseDTO>> listAllCampaigns() {
		List<CampaignResponseDTO> campaignDTOs = campaignService.getAllCampaigns();
		HttpStatus status = campaignDTOs.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;

		return new ResponseEntity<>(campaignDTOs.isEmpty() ? null : campaignDTOs, status);
	}

	@Operation(summary = "Buscar campaña por id", description = "Busca una campaña por su id.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Campaña encontrada", content = @Content(schema = @Schema(implementation = Campaign.class))),
			@ApiResponse(responseCode = "404", description = "Campaña no encontrada") })
	@GetMapping("/{id}")
	public ResponseEntity<CampaignResponseDTO> findCampaignById(@PathVariable Long id) {
		CampaignResponseDTO campaignDTO = campaignService.findById(id)
				.orElseThrow(() -> new CampaignNotFoundException(messageService.getMessage("campaign.find.notfound")));
		return ResponseEntity.ok(campaignDTO);
	}

	@Operation(summary = "Eliminar campaña", description = "Elimina una campaña existente por su ID.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Campaña eliminada correctamente"),
			@ApiResponse(responseCode = "404", description = "Campaña no encontrada") })
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCampaign(@PathVariable Long id,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		User user = userService.getUserOrThrow(userDetails.getUsername());

		campaignService.deleteCampaign(id, user);
		return ResponseEntity.ok(messageService.getMessage("campaign.delete.ok"));
	}

	@Operation(summary = "Actualizar estado de campaña", description = "Permite al Game Master cambiar el estado de su campaña.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
			@ApiResponse(responseCode = "403", description = "No tienes permisos para modificar esta campaña"),
			@ApiResponse(responseCode = "404", description = "Campaña no encontrada") })
	@PutMapping("/{id}/status")
	@PreAuthorize("hasRole('GAME_MASTER')")
	public ResponseEntity<?> updateCampaignStatus(@PathVariable Long id, @RequestParam CampaignStatusEnum newStatus,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		User authenticatedUser = userService.getUserOrThrow(userDetails.getUsername());

		campaignService.updateCampaignStatus(id, newStatus, authenticatedUser);
		return ResponseEntity.ok(messageService.getMessage("campaign.update.state.ok") + newStatus);
	}

	@Operation(summary = "Actualizar campaña", description = "Permite al Game Master actualizar el nombre y la descripción de una campaña existente.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Campaña actualizada correctamente"),
			@ApiResponse(responseCode = "403", description = "No tienes permisos para modificar esta campaña"),
			@ApiResponse(responseCode = "404", description = "Campaña no encontrada"),
			@ApiResponse(responseCode = "400", description = "Datos inválidos") })
	@PutMapping("/{id}/update")
	@PreAuthorize("hasRole('GAME_MASTER')")
	public ResponseEntity<?> updateCampaignDetails(@PathVariable Long id,
			@RequestBody @Valid CampaignRequestDTO request, @AuthenticationPrincipal CustomUserDetails userDetails) {

		User authenticatedUser = userService.getUserOrThrow(userDetails.getUsername());

		campaignService.updateCampaignDetails(id, request, authenticatedUser);

		return ResponseEntity.ok(messageService.getMessage("campaign.update.ok"));
	}

	@Operation(summary = "Asignar héroe a campaña", description = "Permite a un jugador asignar un héroe a una campaña. Solo puede tener un héroe por campaña.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Héroe asignado correctamente"),
			@ApiResponse(responseCode = "403", description = "No puedes asignar héroes de otros usuarios"),
			@ApiResponse(responseCode = "409", description = "Ya tienes un héroe en esta campaña") })

	@PostMapping("/{campaignId}/assign-hero")
	@PreAuthorize("hasRole('GAME_MASTER')")
	public ResponseEntity<?> assignHeroToCampaign(@PathVariable Long campaignId, @RequestParam Long heroId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		User gm = userService.getUserOrThrow(userDetails.getUsername());
		campaignService.validateGameMaster(gm, campaignId);

		campaignService.addHeroToCampaign(heroId, campaignId, gm);
		return ResponseEntity.ok("Héroe asignado correctamente.");
	}

	@PostMapping("/{campaignId}/remove-hero")
	@PreAuthorize("hasRole('GAME_MASTER')")
	public ResponseEntity<?> removeHeroFromCampaign(@PathVariable Long campaignId, @RequestParam Long heroId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		User gm = userService.getUserOrThrow(userDetails.getUsername());

		campaignService.validateGameMaster(gm, campaignId);
		heroService.kickHeroFromCampaign(heroId, campaignId);
		return ResponseEntity.ok("Héroe expulsado de la campaña.");
	}

	@PutMapping("/{campaignId}/update-hero")
	@PreAuthorize("hasRole('GAME_MASTER')")
	public ResponseEntity<?> updateHeroInCampaign(@PathVariable Long campaignId, @RequestParam Long heroId,
			@RequestBody HeroCampaignRequestDTO request, @AuthenticationPrincipal CustomUserDetails userDetails) {
		User gm = userService.getUserOrThrow(userDetails.getUsername());
		campaignService.validateGameMaster(gm, campaignId);
		heroService.updateHeroCampaign(heroId, campaignId, request);

		return ResponseEntity.ok("Héroe actualizado correctamente en la campaña.");
	}

	@GetMapping("/owner")
	public ResponseEntity<List<CampaignResponseDTO>> getCampaignsOwnedByUser(
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		User user = userService.getUserOrThrow(userDetails.getUsername());
		List<CampaignResponseDTO> dtos = campaignService.getUserCampaigns(user.getId());
		return dtos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(dtos);
	}

	@GetMapping("/participant")
	public ResponseEntity<List<CampaignResponseDTO>> getCampaignsWhereUserIsParticipant(
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		User user = userService.getUserOrThrow(userDetails.getUsername());
		List<CampaignResponseDTO> dtos = campaignService.getCampaignsWhereUserIsParticipant(user.getId());
		return dtos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(dtos);
	}

	@Operation(summary = "Obtener héroes asignados a la campaña", 
	           description = "Devuelve la lista de héroes asignados a la campaña. Solo el Game Master (propietario) puede acceder.")
	@GetMapping("/{campaignId}/heroes")
	@PreAuthorize("hasRole('GAME_MASTER')")
	public ResponseEntity<List<HeroCampaignResponseDTO>> getAssignedHeroes(
	        @PathVariable Long campaignId,
	        @AuthenticationPrincipal CustomUserDetails userDetails) {

	    User gm = userService.getUserOrThrow(userDetails.getUsername());
	    List<HeroCampaignResponseDTO> heroes = campaignService.getAssignedHeroes(campaignId, gm);
	    return ResponseEntity.ok(heroes);
	}


}
