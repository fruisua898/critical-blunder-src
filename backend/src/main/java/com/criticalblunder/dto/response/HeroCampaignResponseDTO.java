package com.criticalblunder.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeroCampaignResponseDTO {
    private Long heroId;
    private String HeroName;
    private Long campaignId;
    private String campaignName;
    private Integer level;
    private Integer experience;
    private String status;
    private String assignedAt;
}
