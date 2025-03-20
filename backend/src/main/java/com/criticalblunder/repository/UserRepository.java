package com.criticalblunder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.criticalblunder.enums.RoleEnum;
import com.criticalblunder.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad {@link User}. Proporciona métodos de acceso a la
 * base de datos y operaciones CRUD estándar a través de la extensión de
 * {@link JpaRepository}.
 * <p>
 * Incluye métodos personalizados para realizar búsquedas y modificaciones
 * específicas en la tabla de usuarios.
 * </p>
 *
 * @author
 * @version 1.0
 * @since 2024
 */
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Busca un usuario por su dirección de correo electrónico.
	 *
	 * @param email El correo electrónico del usuario.
	 * @return Un {@link Optional} que contiene el usuario si se encuentra, o
	 *         {@link Optional#empty()} si no existe un usuario con ese correo.
	 */
	Optional<User> findByEmail(String email);

	/**
	 * Verifica si existe un usuario con el correo electrónico especificado.
	 *
	 * @param email El correo electrónico a verificar.
	 * @return {@code true} si existe un usuario con ese correo, {@code false} en
	 *         caso contrario.
	 */
	boolean existsByEmail(String email);

	/**
	 * Actualiza el rol de un usuario con el ID especificado.
	 * <p>
	 * Este método permite cambiar el rol de cualquier usuario proporcionando un
	 * nuevo rol y su identificador único (ID).
	 * </p>
	 * <b>Nota:</b> Este método es de escritura y requiere una transacción activa.
	 *
	 * @param userId El ID del usuario cuyo rol será actualizado.
	 * @param role   El nuevo rol que se asignará al usuario.
	 */
	@Modifying
	@Query("UPDATE User u SET u.role = :role WHERE u.id = :userId")
	void updateUserRole(@Param("userId") Long userId, @Param("role") RoleEnum role);

	/**
	 * Busca usuarios cuyo nombre contenga la cadena dada (ignorando
	 * mayúsculas/minúsculas).
	 *
	 * @param name la cadena a buscar en el nombre del usuario.
	 * @return lista de usuarios que cumplen el criterio.
	 */
	List<User> findByNameContainingIgnoreCase(String name);
}
