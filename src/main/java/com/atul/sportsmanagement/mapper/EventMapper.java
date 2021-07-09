package com.atul.sportsmanagement.mapper;

import com.atul.sportsmanagement.dto.EventRequest;
import com.atul.sportsmanagement.dto.EventResponse;
import com.atul.sportsmanagement.model.Event;
import com.atul.sportsmanagement.model.EventType;
import com.atul.sportsmanagement.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
@Component
public  class EventMapper {
    @Autowired
    private EventRepository eventRepository;
    public Event map(EventRequest eventRequest){
        Event event = new Event();
        event.setEventName(eventRequest.getEventName());
        event.setDescription(eventRequest.getDescription());
        event.setMaxParticipant(eventRequest.getMaxParticipant());
        event.setCreatedDate(Instant.now());
        Instant lastDate = Instant.parse(eventRequest.getLastDate() + "T00:00:00Z");
        Instant eventDate = Instant.parse(eventRequest.getEventDate() + ":00Z");
        event.setLastDate(lastDate);
        event.setEventDate(eventDate);
        event.setCategory(eventRequest.getCategory());
        event.setType(eventRequest.getType().equals("TEAM") ? EventType.TEAM : EventType.INDIVIDUAL);
        event.setLocation(eventRequest.getLocation());
        event.setMaxMembersInTeam(eventRequest.getMaxMembersInTeam());
        event.setVenue(eventRequest.getVenue());
        event.setPicture(eventRequest.getPicture());
       return event;
    }
    public EventResponse mapToDto(Event event){
        EventResponse eventResponse = new EventResponse();
        eventResponse.setEventName(event.getEventName());
        eventResponse.setId(event.getEventId());
        eventResponse.setCategory(event.getCategory());
        eventResponse.setDescription(event.getDescription());
        eventResponse.setMaxParticipant(event.getMaxParticipant());
        eventResponse.setLastDate(event.getLastDate().toString());
        eventResponse.setEventDate(event.getEventDate().toString());
        eventResponse.setType(event.getType().toString());
        eventResponse.setTotalRegistered(event.getParticipants().size());
        eventResponse.setLocation(event.getLocation());
        eventResponse.setPicture(event.getPicture());
        eventResponse.setVenue(event.getVenue());
        eventResponse.setMaxMembersInTeam(event.getMaxMembersInTeam());
        if(event.getWinners()!=null){
            eventResponse.setW1(event.getWinners().getWinner());
            eventResponse.setW2(event.getWinners().getFirstRunnerUp());
            eventResponse.setW3(event.getWinners().getSecondRunnerUp());
        }
        return eventResponse;
    }



}
