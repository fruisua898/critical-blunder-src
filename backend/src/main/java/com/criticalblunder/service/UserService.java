package com.criticalblunder.service;

import java.util.Optional;

import com.criticalblunder.dto.request.UserRequestDTO;
import com.criticalblunder.model.User;

public interface UserService {
	/**
	 * Da de alta a un usuario en la DB.
	 * <p>
	 * Este método mapea el DTO de solicitud de usuario a la entidad de usuario,
	 * codifica la contraseña si está presente y asigna el rol predeterminado antes
	 * de guardar al usuario en la base de datos.
	 * </p>
	 *
	 * @param request DTO que tiene la información del usuario a dar de alta.
	 */
	void registerUser(UserRequestDTO request);



	/**
	 * Busca un usuario por correo electrónico.
	 * <p>
	 * Consulta la base de datos para encontrar el usuario asociado al correo
	 * proporcionado.
	 * </p>
	 *
	 * @param email El correo electrónico del usuario.
	 * @return Un {@code Optional<User>} que contiene el usuario si existe, o vacío
	 *         si no hay ningún usuario registrado con ese correo.
	 */
	Optional<User> findByEmail(String email);

	/**
	 * Verifica si existe un usuario registrado con la dirección de correo
	 * electrónico proporcionada.
	 * <p>
	 * Validación para comprobar si el correo está en uso.
	 * </p>
	 *
	 * @param email Correo electrónico a verificar.
	 * @return {@code true} si existe un usuario con ese correo, {@code false} en
	 *         caso contrario.
	 */
	boolean existsByEmail(String email);

	/**
	 * Simplemente comprueba si el usuario es PLAYER
	 * <p>
	 * Este método es utilizado como una comprobación previa antes de promover a un
	 * usuario al rol de GAME_MASTER. Se considera que un usuario es un jugador
	 * (PLAYER) si su rol actual coincide con el rol por defecto definido en
	 * {@link RoleConstants#DEFAULT_ROLE}.
	 * </p>
	 * 
	 * @param user El usuario cuyo rol será verificado.
	 * @return {@code true} si el usuario tiene el rol de PLAYER, {@code false} en
	 *         caso contrario.
	 */
	boolean isPlayer(User user);

	/**
	 * Actualiza el rol de un usuario, permitiendo ascender o descender su nivel de permisos.
	 * <p>
	 * Si el parámetro {@code ascenso} es {@code true}, el usuario será promovido al rol de 
	 * Game Master (o equivalente definido en {@link RoleConstants#CAMPAIGN_OWNER}). 
	 * Si es {@code false}, el usuario será degradado al rol por defecto de jugador 
	 * ({@link RoleConstants#DEFAULT_ROLE}).
	 * </p>
	 * <p>
	 * Este método también actualiza el objeto en memoria para reflejar el cambio de rol
	 * de forma inmediata.
	 * </p>
	 *
	 * @param gameMaster El usuario cuyo rol será actualizado.
	 * @param ascenso {@code true} para ascender al usuario, {@code false} para degradarlo.
	 */
	void updateUserRole(User gameMaster, Boolean Ascenso);



	User getUserOrThrow(String email);

}
