package com.criticalblunder.controller;

import com.criticalblunder.dto.request.HeroRequestDTO;
import com.criticalblunder.dto.request.HeroRequestUpdateDTO;
import com.criticalblunder.dto.response.HeroResponseDTO;
import com.criticalblunder.model.Campaign;
import com.criticalblunder.model.User;
import com.criticalblunder.service.HeroService;
import com.criticalblunder.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import security.CustomUserDetails;

import java.util.List;

@RestController
@RequestMapping("/api/heroes")
public class HeroController {

    private final HeroService heroService;
    private final UserService userService;

    public HeroController(HeroService heroService, UserService userService) {
        this.heroService = heroService;
        this.userService = userService;
    }

    @Operation(summary = "Crear héroe", description = "Permite a los jugadores crear un nuevo héroe con las clases clásicas de DnD.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Héroe creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos") 
    })
    @PostMapping("/create")
    public ResponseEntity<?> createHero(@RequestBody @Valid HeroRequestDTO request,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userService.getUserOrThrow(userDetails.getUsername());
        heroService.createHero(request, user);
        return ResponseEntity.ok("Héroe creado satisfactoriamente.");
    }

    @DeleteMapping("/{heroId}")
    public ResponseEntity<?> deleteHero(@PathVariable Long heroId,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userService.getUserOrThrow(userDetails.getUsername());
        heroService.deleteHero(heroId, user);
        return ResponseEntity.ok("Héroe eliminado correctamente.");
    }

    @GetMapping("/{heroId}/campaigns")
    public ResponseEntity<List<Campaign>> listCampaignsForHero(@PathVariable Long heroId) {
        List<Campaign> campaigns = heroService.getCampaignsForHero(heroId);
        return ResponseEntity.ok(campaigns);
    }

    @PostMapping("/{heroId}/leave")
    public ResponseEntity<?> leaveCampaign(@PathVariable Long heroId, @RequestParam Long campaignId,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userService.getUserOrThrow(userDetails.getUsername());
        heroService.leaveCampaign(heroId, campaignId, user);
        return ResponseEntity.ok("Héroe retirado de la campaña.");
    }

    @PutMapping("/{heroId}")
    public ResponseEntity<?> updateHero(@PathVariable Long heroId, @RequestBody HeroRequestUpdateDTO request,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        User player = userService.getUserOrThrow(userDetails.getUsername());
        heroService.validateHeroOwnership(heroId, player);
        heroService.updateHero(heroId, request, player);
        return ResponseEntity.ok("Héroe actualizado correctamente.");
    }

    @Operation(summary = "Listar todos los héroes del usuario", 
               description = "Obtiene todos los héroes creados por el usuario autenticado, sin importar si están en una campaña o no.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Lista de héroes obtenida correctamente"),
        @ApiResponse(responseCode = "204", description = "El usuario no tiene héroes"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado") 
    })
    @GetMapping("/user")
    public ResponseEntity<List<HeroResponseDTO>> getUserHeroes(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userService.getUserOrThrow(userDetails.getUsername());
        List<HeroResponseDTO> heroes = heroService.getUserHeroes(user);
        return heroes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(heroes);
    }

    @Operation(summary = "Obtener héroe por ID", description = "Obtiene un héroe por su ID.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Héroe retornado correctamente"),
        @ApiResponse(responseCode = "404", description = "El héroe no existe") 
    })
    @GetMapping("/{heroId:\\d+}")
    public ResponseEntity<HeroResponseDTO> getHero(@PathVariable Long heroId,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        User player = userService.getUserOrThrow(userDetails.getUsername());
        heroService.validateHeroOwnership(heroId, player);
        HeroResponseDTO heroResponse = heroService.getHeroResponse(heroId);
        return ResponseEntity.ok(heroResponse);
    }

    @Operation(summary = "Obtener héroes de un usuario", 
               description = "Obtiene todos los héroes asociados al usuario cuyo ID se pasa como parámetro.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Lista de héroes obtenida correctamente"),
        @ApiResponse(responseCode = "404", description = "Usuario o héroes no encontrados") 
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HeroResponseDTO>> getHeroesByUserId(@PathVariable Long userId) {
        List<HeroResponseDTO> heroes = heroService.getHeroesByUserId(userId);
        return heroes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(heroes);
    }

    @Operation(summary = "Buscar héroes", 
               description = "Obtiene la lista de héroes del usuario autenticado cuyo nombre contiene el valor especificado y, opcionalmente, filtra por la clase del héroe. Si 'heroClass' es nulo o vacío, se ignora el filtro de clase.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de héroes encontrada correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/search")
    public ResponseEntity<List<HeroResponseDTO>> searchHeroes(
            @RequestParam String name,
            @RequestParam(required = false, defaultValue = "ALL") String heroClass) {
        List<HeroResponseDTO> heroes = heroService.searchHeroesByNameAndHeroClass(name, heroClass);
        if (heroes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(heroes);
    }
}
    
    

