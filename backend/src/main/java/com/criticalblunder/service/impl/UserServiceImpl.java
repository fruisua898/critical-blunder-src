package com.criticalblunder.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.criticalblunder.dto.request.UserRequestDTO;
import com.criticalblunder.enums.RoleEnum;
import com.criticalblunder.exception.DuplicateUserException;
import com.criticalblunder.model.User;
import com.criticalblunder.repository.UserRepository;
import com.criticalblunder.service.MessageService;
import com.criticalblunder.service.UserService;

import jakarta.transaction.Transactional;
import mapper.UserMapper;

import com.criticalblunder.constants.RoleConstants;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;
	private final MessageService messageService;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper usermapper,
			MessageService messageService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.userMapper = usermapper;
		this.messageService = messageService;
	}

	@Override
	public void registerUser(UserRequestDTO request) {
		User user = userMapper.toEntity(request);

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new DuplicateUserException("El correo ya está registrado.");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		user.setRole(RoleConstants.DEFAULT_ROLE);

		userRepository.save(user);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	@Transactional
	public void updateUserRole(User gameMaster, Boolean promote) {
		RoleEnum newRole = promote ? RoleConstants.CAMPAIGN_OWNER : RoleConstants.DEFAULT_ROLE;
		if (promote && !isPlayer(gameMaster))
			return;
//		Decisión de diseño: Se utiliza un early return en el método updateUserRole para cortar la ejecución en caso de que no se cumplan las condiciones necesarias.
//
//		Al tratarse de un método void, el return no afecta el flujo global del programa, sino que simplemente evita continuar con operaciones innecesarias.
//		Esta práctica reduce anidaciones, mejora la legibilidad y simplifica el código, siguiendo el principio de claridad y mantenimiento a largo plazo.*/
		userRepository.updateUserRole(gameMaster.getId(), newRole);
		gameMaster.setRole(newRole);
	}

	@Override
	public boolean isPlayer(User user) {
		return user.getRole() == RoleConstants.DEFAULT_ROLE;
	}

	@Override
	public User getUserOrThrow(String email) {

		return userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException(messageService.getMessage("user.not.found")));
	}
}
