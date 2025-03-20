package com.criticalblunder.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.criticalblunder.model.User;
import com.criticalblunder.enums.RoleEnum;
import com.criticalblunder.service.CustomUserDetailsService;
import com.criticalblunder.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

	@Mock
	private UserService userService;

	@InjectMocks
	private CustomUserDetailsService customUserDetailsService;

	private User user;

	@BeforeEach
	void setUp() {
		user = new User();
		user.setId(1L);
		user.setName("Test User");
		user.setEmail("test@example.com");
		user.setPassword("password123");
		user.setRole(RoleEnum.PLAYER);
	}

	@Test
	void shouldLoadUserByUsernameSuccessfully() {
		when(userService.getUserOrThrow("test@example.com")).thenReturn(user);

		UserDetails userDetails = customUserDetailsService.loadUserByUsername("test@example.com");

		assertNotNull(userDetails);
		assertEquals("test@example.com", userDetails.getUsername());
		assertEquals("password123", userDetails.getPassword());
		assertTrue(userDetails.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_" + user.getRole().name())));

		verify(userService, times(1)).getUserOrThrow("test@example.com");
	}

	@Test
	void shouldThrowExceptionWhenUserNotFound() {
		when(userService.getUserOrThrow("notfound@example.com")).thenThrow(new RuntimeException("User not found"));

		assertThrows(RuntimeException.class, () -> customUserDetailsService.loadUserByUsername("notfound@example.com"));

		verify(userService, times(1)).getUserOrThrow("notfound@example.com");
	}
}