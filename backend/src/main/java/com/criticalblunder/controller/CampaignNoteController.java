package com.criticalblunder.controller;

import com.criticalblunder.dto.request.CampaignNoteRequestDTO;
import com.criticalblunder.model.CampaignNote;
import com.criticalblunder.service.CampaignNoteService;
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
@RequestMapping("/api/notes")
public class CampaignNoteController {

    private final CampaignNoteService campaignNoteService;

    public CampaignNoteController(CampaignNoteService campaignNoteService) {
        this.campaignNoteService = campaignNoteService;
    }

    @Operation(summary = "Crear nota", description = "Crea una nota dentro de una campaña específica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Nota creada correctamente",
                content = @Content(schema = @Schema(implementation = CampaignNote.class))),
        @ApiResponse(responseCode = "403", description = "No tienes permisos para modificar esta campaña"),
        @ApiResponse(responseCode = "404", description = "Campaña no encontrada")
    })
    @PostMapping("/{campaignId}/create")
    @PreAuthorize("hasRole('GAME_MASTER')")
    public CampaignNote createNote(@PathVariable Long campaignId,
                                   @RequestBody @Valid CampaignNoteRequestDTO request,
                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        return campaignNoteService.createNote(campaignId, request, userDetails.getUsername());
    }

    @Operation(summary = "Listar notas por campaña", description = "Obtiene una lista de notas asociadas a una campaña específica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de notas obtenida correctamente",
                content = @Content(schema = @Schema(implementation = CampaignNote.class))),
        @ApiResponse(responseCode = "404", description = "Campaña no encontrada")
    })
    @GetMapping("/{campaignId}")
    public List<CampaignNote> listNotesByCampaign(@PathVariable Long campaignId) {
        return campaignNoteService.getNotesByCampaign(campaignId);
    }

    @Operation(summary = "Actualizar nota", description = "Permite al autor actualizar una nota específica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Nota actualizada correctamente",
                content = @Content(schema = @Schema(implementation = CampaignNote.class))),
        @ApiResponse(responseCode = "403", description = "No tienes permisos para modificar esta nota"),
        @ApiResponse(responseCode = "404", description = "Nota no encontrada")
    })
    @PutMapping("/{noteId}")
    @PreAuthorize("hasRole('GAME_MASTER')")
    public CampaignNote updateNote(@PathVariable Long noteId,
                                   @RequestBody @Valid CampaignNoteRequestDTO request,
                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        return campaignNoteService.updateNote(noteId, request, userDetails.getUsername());
    }

    @Operation(summary = "Eliminar nota", description = "Permite al autor o al Game Master eliminar una nota específica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Nota eliminada correctamente"),
        @ApiResponse(responseCode = "403", description = "No tienes permisos para eliminar esta nota"),
        @ApiResponse(responseCode = "404", description = "Nota no encontrada")
    })
    @DeleteMapping("/{noteId}")
    @PreAuthorize("hasRole('GAME_MASTER')")
    public String deleteNote(@PathVariable Long noteId,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        campaignNoteService.deleteNote(noteId, userDetails.getUsername());
        return "Nota eliminada correctamente.";
    }
}
