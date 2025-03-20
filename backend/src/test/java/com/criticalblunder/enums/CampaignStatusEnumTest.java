package com.criticalblunder.enums;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CampaignStatusEnumTest {

	@Test
	void shouldHaveValidCampaignStatuses() {
		assertEquals("ACTIVE", CampaignStatusEnum.ACTIVE.name());
		assertEquals("FINISHED", CampaignStatusEnum.FINISHED.name());
		assertEquals("PAUSED", CampaignStatusEnum.PAUSED.name());
	}

	@Test
	void shouldConvertStringToEnum() {
		assertEquals(CampaignStatusEnum.ACTIVE, CampaignStatusEnum.valueOf("ACTIVE"));
		assertEquals(CampaignStatusEnum.FINISHED, CampaignStatusEnum.valueOf("FINISHED"));
		assertEquals(CampaignStatusEnum.PAUSED, CampaignStatusEnum.valueOf("PAUSED"));
	}

	@Test
	void shouldThrowExceptionForInvalidStatus() {
		assertThrows(IllegalArgumentException.class, () -> CampaignStatusEnum.valueOf("CANCELLED"));
	}
}