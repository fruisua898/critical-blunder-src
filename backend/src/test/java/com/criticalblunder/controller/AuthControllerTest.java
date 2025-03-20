package com.criticalblunder.controller;

import com.criticalblunder.dto.request.LoginRequestDTO;
import com.criticalblunder.dto.request.UserRequestDTO;
import com.criticalblunder.dto.response.AuthResponseDTO;
import com.criticalblunder.service.UserService;
import security.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private JwtUtil jwtUtil;

	@Mock
	private UserService userService;

	@InjectMocks
	private AuthController authController;

	private LoginRequestDTO loginRequest;
	private UserRequestDTO userRequest;
	private Authentication authentication;
	private UserDetails userDetails;

	@BeforeEach
	void setUp() {
		loginRequest = new LoginRequestDTO("test@email.com", "password123");
		userRequest = new UserRequestDTO("test@email.com", "password123", "Test User");
		userDetails = new User("test@email.com", "password123", Collections.emptyList());
		authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
		lenient().when(authenticationManager.authenticate(any())).thenReturn(authentication);
		lenient().when(jwtUtil.generateToken(anyString())).thenReturn("mocked_jwt_token");
	}

	@Test
	void loginSucess() {
		AuthResponseDTO response = authController.login(loginRequest);

		assertNotNull(response);
		assertEquals("mocked_jwt_token", response.getToken());
		verify(authenticationManager, times(1)).authenticate(any());
		verify(jwtUtil, times(1)).generateToken(anyString());
	}

	@Test
	void registerSucess() {
		doNothing().when(userService).registerUser(any(UserRequestDTO.class));

		ResponseEntity<String> response = authController.register(userRequest);

		assertNotNull(response);
		assertEquals(200, response.getStatusCode().value());
		assertEquals("Usuario registrado satisfactoriamente.", response.getBody());
		verify(userService, times(1)).registerUser(any(UserRequestDTO.class));
	}
}