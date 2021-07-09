package com.atul.sportsmanagement.controller;

import com.atul.sportsmanagement.dto.*;
import com.atul.sportsmanagement.exceptions.*;
import com.atul.sportsmanagement.mapper.EventMapper;
import com.atul.sportsmanagement.model.Event;
import com.atul.sportsmanagement.model.Schedule;
import com.atul.sportsmanagement.service.EventService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequestMapping("/api/events")
@AllArgsConstructor
@Slf4j
public class EventController {
    private final EventService eventService;
    private final EventMapper mapper;


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> createEvent(@RequestBody EventRequest eventRequest){
        eventService.save(eventRequest);
         return new ResponseEntity<>(CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/published")
    public ResponseEntity<List<EventResponse>> getPublishedEvents(){
        return new ResponseEntity<>(eventService.getAllPublishedEvents(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public EventResponse getEvent(@PathVariable Long id){
        Event event = eventService.getEventById(id);
        return mapper.mapToDto(event);
    }

    @GetMapping
    public List<EventResponse> getAllEvents(@RequestParam(name = "filter",required = false,defaultValue = "all") String sort){
        return eventService.getAllEvents(sort);
    }

    @PutMapping("/{id}/register")
    public ResponseEntity<Void> registerForEvent(@PathVariable Long id) throws AlreadyJoinedException, NoEmptyPlaceException {
        eventService.joinToSportEvent(id);
        return new ResponseEntity<>(CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<Set<EventResponse>> getMyEvents(){
        Set<EventResponse> userRegisteredEvents = eventService.getMyEvents();
        return new ResponseEntity<>(userRegisteredEvents, OK);
    }

    @PutMapping("/{id}/leave")
    public ResponseEntity<Object> leaveFromEvent(@PathVariable Long id) throws NotParticipatedException,EventNotFoundException {
        eventService.leaveFromEvent(id);
        return new ResponseEntity<>(OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/winners")
    public ResponseEntity<Void> setWinners(@PathVariable Long id, @RequestBody WinnersRequest winnersRequest) throws RegisterAsAdminException,EventNotFoundException {
        eventService.setWinners(id,winnersRequest);
        return new ResponseEntity<>(CREATED);
    }

    @GetMapping("/{id}/winners")
    public ResponseEntity<WinnersResponse> getWinners(@PathVariable Long id) throws WinnersNotDeclaredException,EventNotFoundException{
        return new ResponseEntity<>(eventService.getWinners(id), OK);
    }
    @GetMapping("/winners")
    public ResponseEntity<List<EventResponse>> getEventsAndWinners(){
        return new ResponseEntity<>(eventService.getEventAndWinners(),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id){
        eventService.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/draft")
    public ResponseEntity<EventResponse> saveEventAsDraft(@RequestBody EventRequest eventRequest){
        return new ResponseEntity<>(eventService.saveEventAsDraft(eventRequest),OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/draft/edit/{id}")
    public ResponseEntity<Void> editDraftEvent(@RequestBody EventRequest eventRequest, @PathVariable Long id){
        eventService.editDraftEvent(eventRequest,id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/draft/publish/{id}")
    public ResponseEntity<Void> saveDraftAsEvent(@PathVariable Long id){
        eventService.saveDraftAsEvent(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/draft")
    public ResponseEntity<List<EventResponse>> getDraftEvents(){
        return new ResponseEntity<>(eventService.getDraftEvents(), OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/expired")
    public ResponseEntity<List<EventResponse>> getExpiredEvents(){
        return new ResponseEntity<>(eventService.getExpiredEvents(), OK);
    }
    @PutMapping(value = "{id}/register/team",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InviteLink> registerAsTeam(@PathVariable Long id,@RequestBody TeamRequest teamRequest) throws AlreadyJoinedException {
        return eventService.registerAsTeam(id,teamRequest);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/registered")
    public ResponseEntity<Set<UserResponse>> getParticipatedUsers(@PathVariable Long id){
        return new ResponseEntity<>(eventService.getParticipatedUsers(id),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/schedule/draft")
    public ResponseEntity<Schedule> generateSchedule(@RequestParam(name = "method", defaultValue = "se") String methodName, @PathVariable Long id){
        return new ResponseEntity<>(eventService.generateSchedule(methodName,id),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/schedule/publish")
    public ResponseEntity<Boolean> publishSchedule(@RequestParam(name = "method", defaultValue = "se") String methodName, @PathVariable Long id){
        return new ResponseEntity<>(eventService.publishSchedule(methodName,id),HttpStatus.OK);
    }
    @GetMapping("/{id}/schedule")
    public ResponseEntity<Schedule> viewSchedule(@PathVariable Long id){
        return new ResponseEntity<>(eventService.viewSchedule(id),HttpStatus.OK);
    }
}
