package com.criticalblunder.exception;

public class DuplicateCampaignNameException extends RuntimeException  {


	private static final long serialVersionUID = -3287432331054307258L;

	public DuplicateCampaignNameException(String message) {
        super(message);
    }
}
