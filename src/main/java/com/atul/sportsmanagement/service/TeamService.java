package com.atul.sportsmanagement.service;

import com.atul.sportsmanagement.config.AppConfig;
import com.atul.sportsmanagement.dto.TeamResponse;
import com.atul.sportsmanagement.exceptions.AlreadyJoinedException;
import com.atul.sportsmanagement.exceptions.EventNotFoundException;
import com.atul.sportsmanagement.exceptions.SpringSportsException;
import com.atul.sportsmanagement.mapper.TeamMapper;
import com.atul.sportsmanagement.model.Event;
import com.atul.sportsmanagement.model.EventType;
import com.atul.sportsmanagement.model.Teams;
import com.atul.sportsmanagement.model.User;
import com.atul.sportsmanagement.repository.EventRepository;
import com.atul.sportsmanagement.repository.TeamsRepository;
import com.atul.sportsmanagement.repository.UserRepository;
import com.atul.sportsmanagement.utils.TokenUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class TeamService {
    private final TeamsRepository teamsRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final TeamMapper teamMapper;
    private final MailService mailService;
    private final EventRepository eventRepository;
    private final AppConfig appConfig;

    public void joinTeam(Long teamId,Long userId) throws AlreadyJoinedException {
        Teams team = teamsRepository.findById(teamId).orElseThrow(() -> new SpringSportsException("No team with this id"));
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("No user with this id"));
        Event event = eventRepository.findById(team.getEventId()).orElseThrow(() -> new EventNotFoundException("No event with this id"));
        if (!team.getMembers().contains(user) && team.getMembers().size() <= event.getMaxMembersInTeam()-1) {
            team.getMembers().add(user);
            teamsRepository.save(team);
        } else {
            throw new AlreadyJoinedException("Seems like you had already joined or team is full");
        }
        System.out.println(user.getUsername()+" "+team.getMembers()+" "+team.getName());
    }
    public TeamResponse getMembers(Long id){
        Teams team = teamsRepository.getById(id);
        return teamMapper.mapToDto(team);
    }
    public Set<TeamResponse> geTeamsByEventId(Long id){
        Set<Teams> teams = teamsRepository.findTeamsByEventId(id).orElseThrow(() ->  new SpringSportsException("No teams registered for this event Id"));
        return teams.stream()
                .map(teamMapper::mapToDto)
                .collect(Collectors.toSet());
    }


    public Set<TeamResponse> getMyTeams() {
        User user = authService.getCurrentUser();
        Set<Teams> teamsByAdminUserId = teamsRepository.findTeamsByAdminUserId(user.getUserId()).orElseThrow(() -> new SpringSportsException("No Teams for this user"));
        return teamsByAdminUserId.stream()
                .map(teamMapper::mapToDto).collect(Collectors.toSet());
    }
    public void requestTeamAdminToAcceptInTeam(Long teamId,Long eventId){
        User user = authService.getCurrentUser();
        String name = user.getUsername();
        Teams team = teamsRepository.findById(teamId).orElseThrow(() -> new SpringSportsException("No team with this id"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("No event with this id"));
        if(event.getType().equals(EventType.TEAM) && team.getMembers().size() <=event.getMaxMembersInTeam()-1){
            String token = TokenUtils.generateToken(user.getUserId()+"!"+teamId);
            String recipient = user.getEmail();
            String teamName = team.getName();
            String eventName = event.getEventName();
            String url = appConfig.getUrl()+"/api/teams/"+token+"/join";
            mailService.prepareAndSend(recipient,name,teamName,eventName,url);
        }
        else{
            throw new SpringSportsException("Sorry team is full");
        }

    }


    public void processJoinTeam(String token, boolean accept) throws AlreadyJoinedException {
        if(accept){
            String[] parseToken = TokenUtils.parseUserIdAndTeamId(token);
            Long userId = Long.parseLong(parseToken[0]);
            Long teamId = Long.parseLong(parseToken[1]);
            joinTeam(teamId,userId);
        }
    }

    public boolean processJoinTeamByInvite(String token) throws AlreadyJoinedException {
          User user = authService.getCurrentUser();
          Long teamId = Long.parseLong(TokenUtils.parseTeamId(token));

          joinTeam(teamId,user.getUserId());
          return true;
        }
}
