package com.criticalblunder.exception;

public class CampaignNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 8173721686492951527L;

	public CampaignNotFoundException(String message) {
        super(message);
    }
}
