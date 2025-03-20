package com.criticalblunder.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HeroCampaignIdTest {

	@Test
	void shouldCreateHeroCampaignIdCorrectly() {
		HeroCampaignId id = new HeroCampaignId(1L, 2L);

		assertNotNull(id);
		assertEquals(1L, id.getHeroId());
		assertEquals(2L, id.getCampaignId());
	}

	@Test
	void shouldBeEqualForSameValues() {
		HeroCampaignId id1 = new HeroCampaignId(1L, 2L);
		HeroCampaignId id2 = new HeroCampaignId(1L, 2L);

		assertEquals(id1, id2);
	}

	@Test
	void shouldNotBeEqualForDifferentHeroId() {
		HeroCampaignId id1 = new HeroCampaignId(1L, 2L);
		HeroCampaignId id2 = new HeroCampaignId(3L, 2L);

		assertNotEquals(id1, id2);
	}

	@Test
	void shouldNotBeEqualForDifferentCampaignId() {
		HeroCampaignId id1 = new HeroCampaignId(1L, 2L);
		HeroCampaignId id2 = new HeroCampaignId(1L, 3L);

		assertNotEquals(id1, id2);
	}

	@Test
	void shouldNotBeEqualToNullOrDifferentClass() {
		HeroCampaignId id = new HeroCampaignId(1L, 2L);

		assertNotEquals(null, id);
		assertNotEquals("some string", id);
	}

	@Test
	void shouldHaveSameHashCodeForEqualObjects() {
		HeroCampaignId id1 = new HeroCampaignId(1L, 2L);
		HeroCampaignId id2 = new HeroCampaignId(1L, 2L);

		assertEquals(id1.hashCode(), id2.hashCode());
	}

	@Test
	void shouldHaveDifferentHashCodeForDifferentObjects() {
		HeroCampaignId id1 = new HeroCampaignId(1L, 2L);
		HeroCampaignId id2 = new HeroCampaignId(2L, 1L);

		assertNotEquals(id1.hashCode(), id2.hashCode());
	}

	@Test
	void shouldNotBeEqualIfHeroIdIsNull() {
		HeroCampaignId id1 = new HeroCampaignId(null, 2L);

		assertNotEquals(id1, null);
	}

	@Test
	void shouldNotBeEqualIfCampaignIdIsNull() {
		HeroCampaignId id1 = new HeroCampaignId(1L, null);

		assertNotEquals(id1, null);
	}

	@Test
	void shouldHandleNullComparisonInEquals() {
		HeroCampaignId id = new HeroCampaignId(1L, 2L);
		assertFalse(id.equals(null));
	}

	@Test
	void shouldReturnTrueForSameInstance() {
		HeroCampaignId id = new HeroCampaignId(1L, 2L);
		assertTrue(id.equals(id));
	}
}