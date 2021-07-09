package com.atul.sportsmanagement.service;

import com.atul.sportsmanagement.dto.*;
import com.atul.sportsmanagement.exceptions.*;
import com.atul.sportsmanagement.mapper.EventMapper;
import com.atul.sportsmanagement.mapper.TeamMapper;
import com.atul.sportsmanagement.mapper.UserMapper;
import com.atul.sportsmanagement.mapper.WinnersMapper;
import com.atul.sportsmanagement.model.*;
import com.atul.sportsmanagement.repository.EventRepository;
import com.atul.sportsmanagement.repository.ScheduleRepository;
import com.atul.sportsmanagement.repository.TeamsRepository;
import com.atul.sportsmanagement.repository.UserRepository;
import com.atul.sportsmanagement.utils.DateUtils;
import com.atul.sportsmanagement.utils.ScheduleUtils;
import com.atul.sportsmanagement.utils.TokenUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class EventService {
    private final EventMapper eventMapper;
    private final WinnersMapper winnersMapper;
    private final EventRepository eventRepository;
    private final AuthService authService;
    private final TeamsRepository teamsRepository;
    private final UserMapper userMapper;
    private final TeamMapper teamMapper;
    private final NotificationService notificationService;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public void save(EventRequest eventRequest) {
        Event event = eventMapper.map(eventRequest);
        event.setIsPublished(true);
        Event res = eventRepository.save(event);
        eventMapper.mapToDto(res);
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("No Event with id: " + id));
    }

    public List<EventResponse> getAllEvents(String filter) {
        User user = authService.getCurrentUser();
        List<Event> responses = eventRepository.findAllByOrderByCreatedDateDesc()
                .parallelStream()
                .filter((event) -> event.getEventDate().isAfter(LocalDateTime.now().toInstant(ZoneOffset.UTC)))
                .filter((event) -> !event.getParticipants().contains(user))
                .filter((event) -> !teamsRepository.findTeamsByAdminUserIdAndEventId(user.getUserId(), event.getEventId()).isPresent())
                .filter((event) -> event.getTeamsParticipated().parallelStream().noneMatch((team) -> team.getMembers().contains(user)))
                .collect(Collectors.toList());
        List<EventResponse> filteredEvents = new ArrayList<>();
        switch (filter) {
            case "all":
                filteredEvents = responses.stream().map(eventMapper::mapToDto).collect(Collectors.toList());
                break;
            case "thisWeek":
                filteredEvents = responses.stream().filter((event) -> DateUtils.thisWeek(event.getEventDate())).map(eventMapper::mapToDto).collect(Collectors.toList());
                break;
            case "thisMonth":
                filteredEvents = responses.stream().filter((event) -> DateUtils.thisMonth(event.getEventDate())).map(eventMapper::mapToDto).collect(Collectors.toList());
                break;
            case "nextMonth":
                filteredEvents = responses.stream().filter((event) -> DateUtils.nextMonth(event.getEventDate())).map(eventMapper::mapToDto).collect(Collectors.toList());
                break;
            case "INDIVIDUAL":
                filteredEvents = responses.stream().filter((event -> event.getType().equals(EventType.INDIVIDUAL))).map(eventMapper::mapToDto).collect(Collectors.toList());
                break;
            case "TEAM":
                filteredEvents = responses.stream().filter((event -> event.getType().equals(EventType.TEAM))).map(eventMapper::mapToDto).collect(Collectors.toList());
                break;
            case "INDOOR":
                filteredEvents = responses.stream().filter((event -> event.getVenue().equals("INDOOR"))).map(eventMapper::mapToDto).collect(Collectors.toList());
                break;
            case "OUTDOOR":
                filteredEvents = responses.stream().filter((event -> event.getVenue().equals("OUTDOOR"))).map(eventMapper::mapToDto).collect(Collectors.toList());
                break;
        }
        return filteredEvents;
    }

    public void joinToSportEvent(Long id) throws AlreadyJoinedException, NoEmptyPlaceException {
        Event event = getEventById(id);
        User user = authService.getCurrentUser();
        if (event.getParticipants().size() < event.getMaxParticipant()) {
            if (!event.getParticipants().contains(user)) {
                event.getParticipants().add(user);
                eventRepository.save(event);
            } else {
                throw new AlreadyJoinedException("It seems you are already joined");
            }
        } else {
            throw new NoEmptyPlaceException("Sorry, there is no empty place :(");
        }
    }

    public Set<EventResponse> getMyEvents() {
        User user = authService.getCurrentUser();
        Set<Event> events = user.getEventsImAttending();
        Optional<Set<Teams>> teamsByAdminUserId = teamsRepository.findTeamsByAdminUserId(user.getUserId());
        if (teamsByAdminUserId.isPresent()) {
            Set<Long> userEventsIds = teamsByAdminUserId.get().parallelStream().map(Teams::getEventId).collect(Collectors.toSet());
            userEventsIds.forEach(id -> events.add(eventRepository.findById(id).get()));
        }
        return events.parallelStream().map(eventMapper::mapToDto).collect(Collectors.toSet());
    }

    public void leaveFromEvent(Long id) throws NotParticipatedException, EventNotFoundException {
        Event event = getEventById(id);
        User user = authService.getCurrentUser();
        if (event.getParticipants().contains(user)) {
            event.getParticipants().remove(user);
            eventRepository.save(event);
        } else {
            throw new NotParticipatedException("You are not participated in this event");
        }
    }

    public void setWinners(Long id, WinnersRequest winnersRequest) throws RegisterAsAdminException, EventNotFoundException {
        Event event = getEventById(id);
        Winners winners = winnersMapper.map(winnersRequest, id, event);
        event.setWinners(winners);
        eventRepository.save(event);
        Set<User> users = getUsersRegisteredInEvent(id);
        String message = "Winners of " + event.getEventName() + " announced";
        try {
            notificationService.saveNotificationToUsers(users, message, id, event.getPicture(), event.getEventName());
        } catch (Exception ex) {
            throw new SpringSportsException("Not able to update notification");
        }

    }

    public WinnersResponse getWinners(Long id) throws WinnersNotDeclaredException, EventNotFoundException {
        Event event = getEventById(id);
        Winners winners = event.getWinners();
        if (winners == null) throw new WinnersNotDeclaredException("Winners not declared for this event");
        return WinnersMapper.mapToDto(winners);

    }

    public void deleteEvent(Long id) throws RegisterAsAdminException {
        eventRepository.deleteById(id);
    }


    public EventResponse saveEventAsDraft(EventRequest eventRequest) throws RegisterAsAdminException {
        Event eventDraft = eventMapper.map(eventRequest);
        eventDraft.setIsPublished(false);
        eventRepository.save(eventDraft);
        return eventMapper.mapToDto(eventDraft);
    }

    public void editDraftEvent(EventRequest eventRequest, Long id) throws RegisterAsAdminException {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("No event found with is id"));
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
        eventRepository.save(event);
    }

    public void saveDraftAsEvent(Long id) {
        Event eventUnpublished = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("No event found with this Id"));
        eventUnpublished.setIsPublished(true);
        eventRepository.save(eventUnpublished);
    }

    public List<EventResponse> getDraftEvents() {
        return eventRepository.findAll()
                .stream()
                .filter((event -> !event.getIsPublished()))
                .map(eventMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<EventResponse> getExpiredEvents() {
        return eventRepository.findAll().stream()
                .filter((event -> event.getWinners() == null))
                .filter((event) -> event.getEventDate().isBefore(LocalDateTime.now().toInstant(ZoneOffset.UTC)))
                .map(eventMapper::mapToDto)
                .collect(Collectors.toList());
    }


    public ResponseEntity<InviteLink> registerAsTeam(Long id, TeamRequest teamRequest) throws AlreadyJoinedException {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("No Event by this ID"));
        if (event.getType().equals(EventType.INDIVIDUAL)) {
            throw new SpringSportsException("This event is not for teams");
        }
        User user = authService.getCurrentUser();
        Optional<Teams> userTeam = teamsRepository.findTeamsByAdminUserIdAndEventId(user.getUserId(), id);
        if (userTeam.isPresent()) {
            throw new AlreadyJoinedException("You had already joined for this event");
        } else {
            Teams team = teamMapper.map(teamRequest, user, id);
            Teams save = teamsRepository.save(team);
            event.getTeamsParticipated().add(team);
            eventRepository.save(event);
            // generate a team invite;
            String token = TokenUtils.generateToken(save.getTeamId().toString());
            InviteLink link = new InviteLink();
            String url = "http://localhost:4200/userhome/invite?token=" + token;
            link.setLink(url);
            save.setInviteLink(url);
            teamsRepository.save(save);
            return new ResponseEntity<>(link, HttpStatus.OK);
        }
    }

    public Set<User> getUsersRegisteredInEvent(Long id) {
        Event event = eventRepository.getById(id);
        if (event.getType().equals(EventType.INDIVIDUAL)) {
            return event.getParticipants();
        } else {
            Set<User> users = new HashSet<>();
            event.getTeamsParticipated().forEach((team) -> {
                users.add(userRepository.findById(team.getAdminUserId()).get());
                users.addAll(team.getMembers());
            });
            return users;
        }
    }

    public List<EventResponse> getEventAndWinners() {
        return eventRepository.findAll().stream()
                .filter((event -> event.getWinners() != null))
                .filter((event) -> event.getEventDate().isBefore(LocalDateTime.now().toInstant(ZoneOffset.UTC)))
                .map(eventMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public Set<UserResponse> getParticipatedUsers(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("No event found"));
        return event.getParticipants().stream().map(userMapper::mapToUserResponse).collect(Collectors.toSet());
    }

    public List<EventResponse> getAllPublishedEvents() {
        return eventRepository.findAllByOrderByCreatedDateDesc().stream().filter((event -> event.getLastDate().isAfter(LocalDateTime.now().toInstant(ZoneOffset.UTC)))).filter((event) -> event.getWinners() == null).map(eventMapper::mapToDto).collect(Collectors.toList());
    }

    public Schedule generateSchedule(String methodName, Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("No event by this Id"));
        String[] userNames = null;
        String[] teamNames = null;
        boolean isIndividual = event.getType().equals(EventType.INDIVIDUAL);
        if (isIndividual) {
            List<String> collect = event.getParticipants().stream().map(User::getUsername).collect(Collectors.toList());
            userNames = new String[collect.size()];
            collect.toArray(userNames);
            collect.clear();
        } else {
            List<String> collect = event.getTeamsParticipated().stream().map(Teams::getName).collect(Collectors.toList());
            teamNames = new String[collect.size()];
            collect.toArray(teamNames);
            collect.clear();
        }
        if (methodName.equals("se")) {
            List<Match> matches = ScheduleUtils.generateBySingleElimination(isIndividual ? userNames.length : teamNames.length, isIndividual ? userNames : teamNames);
            Schedule schedule = new Schedule();
            schedule.setEventId(id);
            schedule.setMatches(matches);
            return schedule;
        }
        return new Schedule();
    }

    public boolean publishSchedule(String methodName, Long id) {
        try {
            Optional<Schedule> schedule = scheduleRepository.findByEventId(id);
            if (schedule.isPresent()) {
                throw new SpringSportsException("Schedule for this event is already announced");
            } else {
                scheduleRepository.save(generateSchedule(methodName, id));
                Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("No event by this Id"));
                Set<User> users = getUsersRegisteredInEvent(id);
                String message = "Schedule for " + event.getEventName() + " announced";
                notificationService.saveNotificationToUsers(users, message, id, event.getPicture(), event.getEventName());
                return true;
            }
        } catch (Exception ex) {
            throw new SpringSportsException("Unable to publish event schedule at this time");
        }

    }

    public Schedule viewSchedule(Long id) {
        return scheduleRepository.findByEventId(id).orElseThrow(() -> new SpringSportsException("No Schedule Announced for this Event"));
    }
}

