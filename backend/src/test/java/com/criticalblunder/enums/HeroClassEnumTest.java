package com.criticalblunder.enums;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class HeroClassEnumTest {

	@Test
	void shouldHaveCorrectNumberOfClasses() {
		assertEquals(12, HeroClassEnum.values().length);
	}

	@Test
	void shouldContainSpecificClasses() {
		assertTrue(HeroClassEnum.valueOf("BARBARIAN") == HeroClassEnum.BARBARIAN);
		assertTrue(HeroClassEnum.valueOf("WIZARD") == HeroClassEnum.WIZARD);
		assertTrue(HeroClassEnum.valueOf("PALADIN") == HeroClassEnum.PALADIN);
	}

	@Test
	void shouldThrowExceptionForInvalidClass() {
		assertThrows(IllegalArgumentException.class, () -> HeroClassEnum.valueOf("NINJA"));
	}
}