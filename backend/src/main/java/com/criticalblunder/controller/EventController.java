package com.criticalblunder.controller;

import com.criticalblunder.dto.request.EventRequestDTO;
import com.criticalblunder.model.Event;
import com.criticalblunder.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import security.CustomUserDetails;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

	private final EventService eventService;

	public EventController(EventService eventService) {
		this.eventService = eventService;
	}

	@Operation(summary = "Crear evento", description = "Permite al Game Master crear un evento dentro de una campaña específica.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Evento creado correctamente", content = @Content(schema = @Schema(implementation = Event.class))),
			@ApiResponse(responseCode = "403", description = "No tienes permisos para modificar esta campaña"),
			@ApiResponse(responseCode = "404", description = "Campaña no encontrada") })
	@PostMapping("/{campaignId}/create")
	@PreAuthorize("hasRole('GAME_MASTER')")
	public Event createEvent(@PathVariable Long campaignId, @RequestBody @Valid EventRequestDTO request,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		return eventService.createEvent(campaignId, request, userDetails.getUsername());
	}

	@Operation(summary = "Listar eventos por campaña", description = "Obtiene una lista de eventos asociados a una campaña específica.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de eventos obtenida correctamente", content = @Content(schema = @Schema(implementation = Event.class))),
			@ApiResponse(responseCode = "404", description = "Campaña no encontrada") })
	@GetMapping("/{campaignId}")
	public List<Event> listEventsByCampaign(@PathVariable Long campaignId) {
		return eventService.getEventsByCampaign(campaignId);
	}

	@Operation(summary = "Actualizar evento", description = "Permite al Game Master actualizar la información de un evento específico.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Evento actualizado correctamente", content = @Content(schema = @Schema(implementation = Event.class))),
			@ApiResponse(responseCode = "403", description = "No tienes permisos para modificar este evento"),
			@ApiResponse(responseCode = "404", description = "Evento no encontrado") })
	@PutMapping("/{eventId}")
	@PreAuthorize("hasRole('GAME_MASTER')")
	public Event updateEvent(@PathVariable Long eventId, @RequestBody @Valid EventRequestDTO request,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		return eventService.updateEvent(eventId, request, userDetails.getUsername());
	}

	@Operation(summary = "Eliminar evento", description = "Permite al Game Master eliminar un evento específico de una campaña.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Evento eliminado correctamente"),
			@ApiResponse(responseCode = "403", description = "No tienes permisos para eliminar este evento"),
			@ApiResponse(responseCode = "404", description = "Evento no encontrado") })
	@DeleteMapping("/{eventId}")
	@PreAuthorize("hasRole('GAME_MASTER')")
	public String deleteEvent(@PathVariable Long eventId, @AuthenticationPrincipal CustomUserDetails userDetails) {
		eventService.deleteEvent(eventId, userDetails.getUsername());
		return "Evento eliminado correctamente.";
	}
}
