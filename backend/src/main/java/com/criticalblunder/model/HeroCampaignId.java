package com.criticalblunder.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeroCampaignId implements Serializable {

	private static final long serialVersionUID = 2545054259247622313L;

	@Column(name = "hero_id")
	private Long heroId;

	@Column(name = "campaign_id")
	private Long campaignId;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		HeroCampaignId that = (HeroCampaignId) o;
		return heroId.equals(that.heroId) && campaignId.equals(that.campaignId);
	}

	@Override
	public int hashCode() {
		return 31 * heroId.hashCode() + campaignId.hashCode();
	}
}
