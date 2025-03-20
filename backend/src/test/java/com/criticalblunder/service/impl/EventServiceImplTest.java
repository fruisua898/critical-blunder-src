package com.criticalblunder.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.criticalblunder.dto.request.EventRequestDTO;
import com.criticalblunder.model.Campaign;
import com.criticalblunder.model.Event;
import com.criticalblunder.model.User;
import com.criticalblunder.repository.CampaignRepository;
import com.criticalblunder.repository.EventRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

	@Mock
	private EventRepository eventRepository;
	@Mock
	private CampaignRepository campaignRepository;

	@InjectMocks
	private EventServiceImpl eventService;

	private User gameMaster;
	private Campaign campaign;
	private Event event;
	private EventRequestDTO eventRequestDTO;

	@BeforeEach
	void setUp() {
		gameMaster = new User(1L, "John Doe", "john@example.com", "password", null);
		campaign = new Campaign(1L, "Test Campaign", "Description", null, gameMaster, null, null, null);
		event = new Event(1L, campaign, "Test Event", "Event Description", new Date());
		eventRequestDTO = new EventRequestDTO("Updated Event", "Updated Description");
	}

	@Test
	void shouldCreateEventSuccessfully() {
		when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaign));
		when(eventRepository.save(any(Event.class))).thenReturn(event);

		Event createdEvent = eventService.createEvent(1L, eventRequestDTO, "john@example.com");

		assertNotNull(createdEvent);
		assertEquals("Test Event", createdEvent.getTitle());
		verify(eventRepository).save(any(Event.class));
	}

	@Test
	void shouldThrowExceptionWhenCreatingEventWithoutPermission() {
		when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaign));

		assertThrows(SecurityException.class,
				() -> eventService.createEvent(1L, eventRequestDTO, "unauthorized@example.com"));
	}

	@Test
	void shouldGetEventsByCampaign() {
		when(eventRepository.findByCampaignId(1L)).thenReturn(List.of(event));

		List<Event> events = eventService.getEventsByCampaign(1L);

		assertFalse(events.isEmpty());
		assertEquals(1, events.size());
		assertEquals("Test Event", events.get(0).getTitle());
	}

	@Test
	void shouldUpdateEventSuccessfully() {
		when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
		when(eventRepository.save(any(Event.class))).thenReturn(event);

		Event updatedEvent = eventService.updateEvent(1L, eventRequestDTO, "john@example.com");

		assertNotNull(updatedEvent);
		assertEquals("Updated Event", updatedEvent.getTitle());
		verify(eventRepository).save(event);
	}

	@Test
	void shouldThrowExceptionWhenUpdatingEventWithoutPermission() {
		when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

		assertThrows(SecurityException.class,
				() -> eventService.updateEvent(1L, eventRequestDTO, "unauthorized@example.com"));
	}

	@Test
	void shouldDeleteEventSuccessfully() {
		when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

		eventService.deleteEvent(1L, "john@example.com");

		verify(eventRepository).delete(event);
	}

	@Test
	void shouldThrowExceptionWhenDeletingEventWithoutPermission() {
		when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

		assertThrows(SecurityException.class, () -> eventService.deleteEvent(1L, "unauthorized@example.com"));
	}
}
