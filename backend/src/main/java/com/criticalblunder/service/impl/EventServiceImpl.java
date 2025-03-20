package com.criticalblunder.service.impl;

import com.criticalblunder.dto.request.EventRequestDTO;
import com.criticalblunder.model.Campaign;
import com.criticalblunder.model.Event;
import com.criticalblunder.repository.CampaignRepository;
import com.criticalblunder.repository.EventRepository;
import com.criticalblunder.service.EventService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CampaignRepository campaignRepository;

    public EventServiceImpl(EventRepository eventRepository, CampaignRepository campaignRepository) {
        this.eventRepository = eventRepository;
        this.campaignRepository = campaignRepository;
    }

    @Transactional
    @Override
    public Event createEvent(Long campaignId, EventRequestDTO request, String gmEmail) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaña no encontrada."));

        if (!campaign.getGameMaster().getEmail().equals(gmEmail)) {
            throw new SecurityException("No tienes permisos para modificar esta campaña.");
        }

        Event event = new Event();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setCampaign(campaign);
        return eventRepository.save(event);
    }

    @Override
    public List<Event> getEventsByCampaign(Long campaignId) {
        return eventRepository.findByCampaignId(campaignId);
    }

    @Transactional
    @Override
    public Event updateEvent(Long eventId, EventRequestDTO request, String gmEmail) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado."));

        if (!event.getCampaign().getGameMaster().getEmail().equals(gmEmail)) {
            throw new SecurityException("No tienes permisos para modificar este evento.");
        }

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        return eventRepository.save(event);
    }

    @Transactional
    @Override
    public void deleteEvent(Long eventId, String gmEmail) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado."));

        if (!event.getCampaign().getGameMaster().getEmail().equals(gmEmail)) {
            throw new SecurityException("No tienes permisos para eliminar este evento.");
        }

        eventRepository.delete(event);
    }
}
