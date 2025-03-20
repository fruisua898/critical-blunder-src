package com.criticalblunder.controller;

import com.criticalblunder.dto.request.LoginRequestDTO;
import com.criticalblunder.dto.request.UserRequestDTO;
import com.criticalblunder.dto.response.AuthResponseDTO;
import com.criticalblunder.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import security.JwtUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Operation(summary = "Iniciar sesión", description = "Genera un token JWT para autenticación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas"),
            @ApiResponse(responseCode = "403", description = "Cuenta bloqueada o deshabilitada")
    })
    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserDetails user = (UserDetails) auth.getPrincipal();
        String token = jwtUtil.generateToken(user.getUsername());

        return new AuthResponseDTO(token);
    }

    @Operation(summary = "Registrar nuevo usuario", 
            description = "Crea una nueva cuenta de usuario. "
                        + "Debe proporcionar un correo electrónico único y una contraseña válida.")
 @ApiResponses(value = {
     @ApiResponse(responseCode = "200", description = "Usuario registrado satisfactoriamente"),
     @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
     @ApiResponse(responseCode = "409", description = "El correo ya está registrado")
 })
 @PostMapping("/register")
 public ResponseEntity<String> register(@RequestBody @Valid UserRequestDTO request) {
     userService.registerUser(request);
     return ResponseEntity.ok("Usuario registrado satisfactoriamente.");
 }
}