package security;

import static org.junit.jupiter.api.Assertions.*;

import com.criticalblunder.model.User;
import com.criticalblunder.enums.RoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

class CustomUserDetailsTest {

	private User user;
	private CustomUserDetails customUserDetails;

	@BeforeEach
	void setUp() {
		user = new User();
		user.setId(1L);
		user.setEmail("test@example.com");
		user.setPassword("securepassword");
		user.setRole(RoleEnum.GAME_MASTER);

		customUserDetails = new CustomUserDetails(user);
	}

	@Test
	void getAuthoritiesOk() {
		Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();

		assertNotNull(authorities);
		assertEquals(1, authorities.size());
		assertEquals("ROLE_GAME_MASTER", authorities.iterator().next().getAuthority());
	}

	@Test
	void getPasswordOk() {
		assertEquals("securepassword", customUserDetails.getPassword());
	}

	@Test
	void getUsernameOk() {
		assertEquals("test@example.com", customUserDetails.getUsername());
	}

	@Test
	void isAccountNonExpiredTrue() {
		assertTrue(customUserDetails.isAccountNonExpired());
	}

	@Test
	void isAccountNonLockedTrue() {
		assertTrue(customUserDetails.isAccountNonLocked());
	}

	@Test
	void isCredentialsNonExpiredTrue() {
		assertTrue(customUserDetails.isCredentialsNonExpired());
	}

	@Test
	void isEnabledTrue() {
		assertTrue(customUserDetails.isEnabled());
	}
}