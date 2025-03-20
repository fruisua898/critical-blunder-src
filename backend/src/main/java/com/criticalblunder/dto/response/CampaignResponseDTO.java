package com.criticalblunder.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.Date;

import com.criticalblunder.enums.CampaignStatusEnum;

import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CampaignResponseDTO {
    private Long id;
    private String name;
    private CampaignStatusEnum status;
    private String gameMasterName;
    private Date createdAt;
}