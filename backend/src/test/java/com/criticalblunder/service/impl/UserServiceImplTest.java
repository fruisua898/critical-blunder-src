package com.criticalblunder.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.criticalblunder.dto.request.UserRequestDTO;
import com.criticalblunder.enums.RoleEnum;
import com.criticalblunder.exception.DuplicateUserException;
import com.criticalblunder.model.User;
import com.criticalblunder.repository.UserRepository;
import com.criticalblunder.service.MessageService;
import mapper.UserMapper;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private UserMapper userMapper;

	@Mock
	private MessageService messageService;

	@InjectMocks
	private UserServiceImpl userService;

	private User user;
	private UserRequestDTO userRequestDTO;

	@BeforeEach
	void setUp() {
		userRequestDTO = new UserRequestDTO();
		userRequestDTO.setEmail("test@example.com");
		userRequestDTO.setPassword("password123");

		user = new User();
		user.setId(1L);
		user.setEmail("test@example.com");
		user.setPassword("encodedPassword");
		user.setRole(RoleEnum.PLAYER);
	}

//	@Test
//	void shouldRegisterUserSuccessfully() {
//		when(userMapper.toEntity(userRequestDTO)).thenReturn(user);
//		when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
//
//		userService.registerUser(userRequestDTO);
//
//		assertEquals("encodedPassword", user.getPassword());
//		assertEquals(RoleEnum.PLAYER, user.getRole());
//		verify(userRepository, times(1)).save(user);
//	}

	@Test
	void shouldThrowExceptionWhenUserAlreadyExists() {
		when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

		assertThrows(DuplicateUserException.class, () -> userService.registerUser(userRequestDTO));
	}

	@Test
	void shouldFindUserByEmail() {
		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

		Optional<User> foundUser = userService.findByEmail("test@example.com");

		assertTrue(foundUser.isPresent());
		assertEquals("test@example.com", foundUser.get().getEmail());
	}

	@Test
	void shouldReturnTrueIfUserExistsByEmail() {
		when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

		assertTrue(userService.existsByEmail("test@example.com"));
	}

	@Test
	void shouldUpdateUserRoleToGameMaster() {

		userService.updateUserRole(user, true);

		assertEquals(RoleEnum.GAME_MASTER, user.getRole());
	}

	@Test
	void shouldNotUpdateUserRoleIfNotPlayer() {
		user.setRole(RoleEnum.GAME_MASTER);

		userService.updateUserRole(user, true);

		assertEquals(RoleEnum.GAME_MASTER, user.getRole());
		verify(userRepository, never()).updateUserRole(anyLong(), any());
	}

	@Test
	void shouldReturnTrueIfUserIsPlayer() {
		user.setRole(RoleEnum.PLAYER);
		assertTrue(userService.isPlayer(user));
	}

	@Test
	void shouldReturnFalseIfUserIsNotPlayer() {
		user.setRole(RoleEnum.GAME_MASTER);
		assertFalse(userService.isPlayer(user));
	}

	@Test
	void shouldGetUserOrThrowIfExists() {
		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

		User foundUser = userService.getUserOrThrow("test@example.com");

		assertNotNull(foundUser);
		assertEquals("test@example.com", foundUser.getEmail());
	}

	@Test
	void shouldThrowExceptionIfUserNotFound() {
		when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());
		when(messageService.getMessage("user.not.found")).thenReturn("Usuario no encontrado");

		RuntimeException thrown = assertThrows(RuntimeException.class,
				() -> userService.getUserOrThrow("notfound@example.com"));

		assertEquals("Usuario no encontrado", thrown.getMessage());
	}
}