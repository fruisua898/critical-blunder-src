package com.criticalblunder.dto.request;


import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import com.criticalblunder.enums.HeroStatusEnum;

import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeroCampaignRequestDTO {
    private Integer level;
    private Integer experience;
    private HeroStatusEnum status;
}
