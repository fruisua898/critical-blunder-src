package com.criticalblunder.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.criticalblunder.dto.request.EventRequestDTO;
import com.criticalblunder.exception.CampaignNotFoundException;
import java.lang.SecurityException;
import com.criticalblunder.model.Event;
import com.criticalblunder.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import security.CustomUserDetails;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private CustomUserDetails userDetails;

    @BeforeEach
    void setUp() {
        userDetails = mock(CustomUserDetails.class);
        when(userDetails.getUsername()).thenReturn("gm@example.com");
        lenient().when(userDetails.getUsername()).thenReturn("gm@example.com");
    }

    @Test
    void createEventOk() {
        EventRequestDTO request = new EventRequestDTO();
        request.setTitle("New Event");
        request.setDescription("Description of the event.");

        Event event = new Event();
        event.setId(1L);
        event.setTitle("New Event");

        when(eventService.createEvent(anyLong(), any(EventRequestDTO.class), anyString())).thenReturn(event);

        Event response = eventController.createEvent(1L, request, userDetails);

        assertNotNull(response);
        assertEquals("New Event", response.getTitle());
    }

    @Test
    void createEventCampaignNotFound() {
        when(eventService.createEvent(anyLong(), any(EventRequestDTO.class), anyString()))
                .thenThrow(new CampaignNotFoundException("Campaña no encontrada."));

        Exception exception = assertThrows(CampaignNotFoundException.class, () ->
                eventController.createEvent(1L, new EventRequestDTO(), userDetails));

        assertEquals("Campaña no encontrada.", exception.getMessage());
    }

    @Test
    void createEventPermissionDenied() {
        when(eventService.createEvent(anyLong(), any(EventRequestDTO.class), anyString()))
                .thenThrow(new SecurityException("No tienes permisos para modificar esta campaña."));

        Exception exception = assertThrows(SecurityException.class, () ->
                eventController.createEvent(1L, new EventRequestDTO(), userDetails));

        assertEquals("No tienes permisos para modificar esta campaña.", exception.getMessage());
    }

    @Test
    void listEventsByCampaignOk() {
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Evento Test");

        when(eventService.getEventsByCampaign(anyLong())).thenReturn(List.of(event));

        List<Event> response = eventController.listEventsByCampaign(1L);

        assertFalse(response.isEmpty());
        assertEquals("Evento Test", response.get(0).getTitle());
    }

    @Test
    void listEventsByCampaignEmpty() {
        when(eventService.getEventsByCampaign(anyLong())).thenReturn(Collections.emptyList());

        List<Event> response = eventController.listEventsByCampaign(1L);

        assertTrue(response.isEmpty());
    }

    @Test
    void updateEventOk() {
        EventRequestDTO request = new EventRequestDTO();
        request.setTitle("Updated Event");
        request.setDescription("Updated Content");

        Event updatedEvent = new Event();
        updatedEvent.setId(1L);
        updatedEvent.setTitle("Updated Event");

        when(eventService.updateEvent(anyLong(), any(EventRequestDTO.class), anyString())).thenReturn(updatedEvent);

        Event response = eventController.updateEvent(1L, request, userDetails);

        assertNotNull(response);
        assertEquals("Updated Event", response.getTitle());
    }

    @Test
    void updateEventNotFound() {
        when(eventService.updateEvent(anyLong(), any(EventRequestDTO.class), anyString()))
                .thenThrow(new RuntimeException("Evento no encontrado."));

        Exception exception = assertThrows(RuntimeException.class, () ->
                eventController.updateEvent(1L, new EventRequestDTO(), userDetails));

        assertEquals("Evento no encontrado.", exception.getMessage());
    }

    @Test
    void updateEventPermissionDenied() {
        when(eventService.updateEvent(anyLong(), any(EventRequestDTO.class), anyString()))
                .thenThrow(new SecurityException("No tienes permisos para modificar este evento."));

        Exception exception = assertThrows(SecurityException.class, () ->
                eventController.updateEvent(1L, new EventRequestDTO(), userDetails));

        assertEquals("No tienes permisos para modificar este evento.", exception.getMessage());
    }

    @Test
    void deleteEventOk() {
        doNothing().when(eventService).deleteEvent(anyLong(), anyString());

        String response = eventController.deleteEvent(1L, userDetails);

        assertEquals("Evento eliminado correctamente.", response);
    }

    @Test
    void deleteEventNotFound() {
        doThrow(new RuntimeException("Evento no encontrado."))
                .when(eventService).deleteEvent(anyLong(), anyString());

        Exception exception = assertThrows(RuntimeException.class, () ->
                eventController.deleteEvent(1L, userDetails));

        assertEquals("Evento no encontrado.", exception.getMessage());
    }

    @Test
    void deleteEventPermissionDenied() {
        doThrow(new SecurityException("No tienes permisos para eliminar este evento."))
                .when(eventService).deleteEvent(anyLong(), anyString());

        Exception exception = assertThrows(SecurityException.class, () ->
                eventController.deleteEvent(1L, userDetails));

        assertEquals("No tienes permisos para eliminar este evento.", exception.getMessage());
    }
}