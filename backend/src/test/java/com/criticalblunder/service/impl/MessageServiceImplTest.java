package com.criticalblunder.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

	@Mock
	private MessageSource messageSource;

	@InjectMocks
	private MessageServiceImpl messageService;

	@Test
	void shouldReturnMessageForGivenKey() {
		String message = messageService.getMessage("test.key");

		assertEquals(null, message);
		verify(messageSource, times(1)).getMessage("test.key", null, Locale.getDefault());
	}

	@Test
	void shouldReturnDefaultMessageWhenKeyNotFound() {
		when(messageSource.getMessage("unknown.key", null, Locale.getDefault())).thenReturn("Default Message");

		String message = messageService.getMessage("unknown.key");

		assertEquals("Default Message", message);
		verify(messageSource, times(1)).getMessage("unknown.key", null, Locale.getDefault());
	}
}