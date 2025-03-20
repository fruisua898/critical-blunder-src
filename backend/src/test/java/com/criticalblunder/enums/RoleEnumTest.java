package com.criticalblunder.enums;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class RoleEnumTest {

	@Test
	void shouldConvertStringToEnum() {
		assertEquals(RoleEnum.PLAYER, RoleEnum.fromString("PLAYER"));
		assertEquals(RoleEnum.GAME_MASTER, RoleEnum.fromString("GAME_MASTER"));
		assertEquals(RoleEnum.ADMIN, RoleEnum.fromString("ADMIN"));
	}

	@Test
	void shouldConvertEnumToString() {
		assertEquals("PLAYER", RoleEnum.PLAYER.toString());
		assertEquals("GAME_MASTER", RoleEnum.GAME_MASTER.toString());
		assertEquals("ADMIN", RoleEnum.ADMIN.toString());
	}

	@Test
	void shouldThrowExceptionForInvalidString() {
		assertThrows(IllegalArgumentException.class, () -> RoleEnum.fromString("INVALID_ROLE"));
	}
}