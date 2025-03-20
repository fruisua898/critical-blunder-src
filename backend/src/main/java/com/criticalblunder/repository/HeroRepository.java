package com.criticalblunder.repository;

import com.criticalblunder.dto.response.HeroCampaignResponseDTO;
import com.criticalblunder.enums.HeroClassEnum;
import com.criticalblunder.model.Hero;
import com.criticalblunder.model.HeroCampaign;
import com.criticalblunder.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeroRepository extends JpaRepository<Hero, Long> {

	/**
	 * Busca un héroe por su nombre.
	 *
	 * @param name el nombre del héroe.
	 * @return un {@link Optional} que contiene el héroe si se encuentra, o vacío si
	 *         no existe.
	 */
	Optional<Hero> findByName(String name);

	/**
	 * Busca un héroe asociado a un usuario identificado por su email.
	 *
	 * @param email el email del usuario propietario del héroe.
	 * @return un {@link Optional} que contiene el héroe si se encuentra, o vacío si
	 *         no existe.
	 */
	@Query("SELECT h FROM Hero h WHERE h.user.email = :email")
	Optional<Hero> findHeroByUserEmail(@Param("email") String email);

	/**
	 * Busca un héroe directamente por el ID del usuario propietario.
	 *
	 * @param userId el ID del usuario propietario.
	 * @return el héroe asociado al usuario, si existe.
	 */
	Hero findByUserId(Long userId);

	/**
	 * Verifica si existe al menos un héroe asociado al usuario especificado.
	 *
	 * @param userId el ID del usuario.
	 * @return true si existe al menos un héroe, false en caso contrario.
	 */
	boolean existsByUserId(Long userId);

	/**
	 * Verifica si un usuario tiene algún héroe asociado a una campaña específica.
	 *
	 * @param userId     el ID del usuario.
	 * @param campaignId el ID de la campaña.
	 * @return true si el usuario tiene un héroe en la campaña, false en caso
	 *         contrario.
	 */
	@Query("SELECT COUNT(h) > 0 FROM Hero h JOIN HeroCampaign hc ON h.id = hc.hero.id "
			+ "WHERE h.user.id = :userId AND hc.campaign.id = :campaignId")
	boolean existsHeroInCampaignByUserId(@Param("userId") Long userId, @Param("campaignId") Long campaignId);

	List<Hero> findByUser(User user);

	@Query("SELECT h FROM Hero h WHERE h.user.id = :userId")
	List<Hero> findAllByUserId(@Param("userId") Long userId);

	/**
	 * Retorna todos los héroes que pertenecen a la lista de usuarios.
	 *
	 * @param users lista de usuarios propietarios.
	 * @return lista de héroes.
	 */
	List<Hero> findByUserIn(List<User> users);

	/**
	 * Retorna todos los héroes que pertenecen a la lista de usuarios y cuya clase
	 * coincide (ignorando mayúsculas/minúsculas) con la especificada.
	 *
	 * @param users     lista de usuarios propietarios.
	 * @param heroClass la clase del héroe.
	 * @return lista de héroes.
	 */
	List<Hero> findByUserInAndHeroClass(List<User> users, HeroClassEnum heroClass);

	@Query("SELECT hc.hero FROM HeroCampaign hc WHERE hc.campaign.id = :campaignId")
	List<Hero> findHeroesByCampaignId(@Param("campaignId") Long campaignId);

    /**
     * Busca héroes del usuario cuyo nombre contenga (sin importar mayúsculas) el fragmento dado.
     *
     * @param user el usuario propietario de los héroes.
     * @param name el fragmento a buscar en el nombre.
     * @return lista de héroes encontrados.
     */
    List<Hero> findByUserAndNameContainingIgnoreCase(User user, String name);

    /**
     * Busca héroes del usuario cuyo nombre contenga (sin importar mayúsculas) el fragmento dado
     * y cuya clase coincida con la especificada.
     *
     * @param user el usuario propietario de los héroes.
     * @param name el fragmento a buscar en el nombre.
     * @param heroClass la clase del héroe.
     * @return lista de héroes encontrados.
     */
    List<Hero> findByUserAndNameContainingIgnoreCaseAndHeroClass(User user, String name, String heroClass);
    
    
}


