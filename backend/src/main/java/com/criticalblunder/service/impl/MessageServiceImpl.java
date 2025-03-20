package com.criticalblunder.service.impl;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.criticalblunder.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

	private final MessageSource messageSource;

	public MessageServiceImpl(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public String getMessage(String key) {
		return messageSource.getMessage(key, null, Locale.getDefault());
	}
}
