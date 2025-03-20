package com.criticalblunder.model;

import com.criticalblunder.enums.HeroStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hero_campaign")
public class HeroCampaign {

	@EmbeddedId
	private HeroCampaignId id;

	@ManyToOne
	@MapsId("heroId")
	@JoinColumn(name = "hero_id")
	private Hero hero;

	@ManyToOne
	@MapsId("campaignId")
	@JoinColumn(name = "campaign_id")
	private Campaign campaign;

	@Column(nullable = false)
	private Integer level = 1;

	@Column(nullable = false)
	private Integer experience = 0;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private HeroStatusEnum status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "assigned_at", nullable = false, updatable = false)
	private Date assignedAt = new Date();
}
