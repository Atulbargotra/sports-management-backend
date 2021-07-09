package com.atul.sportsmanagement.mapper;

import com.atul.sportsmanagement.dto.TeamRequest;
import com.atul.sportsmanagement.dto.TeamResponse;
import com.atul.sportsmanagement.model.Teams;
import com.atul.sportsmanagement.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@AllArgsConstructor
public class TeamMapper {
    private final UserMapper userMapper;
    public Teams map(TeamRequest teamRequest,User user,Long eventId){
        Teams team = new Teams();
        team.setName(teamRequest.getName().toLowerCase(Locale.ROOT));
        team.setDescription(teamRequest.getDescription());
        team.setEventId(teamRequest.getEventId());
        team.setAdminUserId(user.getUserId());
        team.setCity(teamRequest.getCity());
        team.setContact(teamRequest.getContact());
        team.setMaxMember(teamRequest.getMaxMembers());
        team.setEventId(eventId);
        team.setEmail(user.getEmail());
        return team;
    }
    public TeamResponse mapToDto(Teams team){
        TeamResponse teamResponse = new TeamResponse();
        teamResponse.setId(team.getTeamId());
        teamResponse.setName(team.getName());
        teamResponse.setDescription(team.getDescription());
        teamResponse.setCity(team.getCity());
        teamResponse.setCurrentNumberOfMembers(team.getMembers().size()+1);
        teamResponse.setEmail(team.getEmail());
        return teamResponse;
    }

}
