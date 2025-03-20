package com.criticalblunder.enums;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class HeroStatusEnumTest {

	@Test
	void shouldHaveValidValues() {
		assertEquals("ALIVE", HeroStatusEnum.ALIVE.name());
		assertEquals("DEAD", HeroStatusEnum.DEAD.name());
		assertEquals("RETIRED", HeroStatusEnum.RETIRED.name());
	}

	@Test
	void shouldConvertStringToEnum() {
		assertEquals(HeroStatusEnum.ALIVE, HeroStatusEnum.valueOf("ALIVE"));
		assertEquals(HeroStatusEnum.DEAD, HeroStatusEnum.valueOf("DEAD"));
		assertEquals(HeroStatusEnum.RETIRED, HeroStatusEnum.valueOf("RETIRED"));
	}

	@Test
	void shouldThrowExceptionForInvalidValue() {
		assertThrows(IllegalArgumentException.class, () -> HeroStatusEnum.valueOf("UNKNOWN"));
	}
}