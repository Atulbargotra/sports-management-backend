package com.atul.sportsmanagement.controller;

import com.atul.sportsmanagement.dto.TeamRequest;
import com.atul.sportsmanagement.dto.TeamResponse;
import com.atul.sportsmanagement.exceptions.AlreadyJoinedException;
import com.atul.sportsmanagement.service.TeamService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.Set;

@RestController
@RequestMapping("/api/teams")
@Slf4j
@AllArgsConstructor

public class TeamController {
    private final TeamService teamService;
    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getMember(@PathVariable Long id){
        return new ResponseEntity<>(teamService.getMembers(id),HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Set<TeamResponse>> geTeamsByEventId(@RequestParam("eventId") Long eventId){
        return new ResponseEntity<>(teamService.geTeamsByEventId(eventId),HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<Set<TeamResponse>> getMyTeams(){
        return new ResponseEntity<>(teamService.getMyTeams(),HttpStatus.OK);
    }
    @PutMapping("/{id}/request")
    public ResponseEntity<Void> requestTeamAdminToAcceptInTeam(@PathVariable Long id, @RequestParam(name = "eventId") Long eventId){
        teamService.requestTeamAdminToAcceptInTeam(id,eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/{token}/join")
    public ResponseEntity<String> joinTeam(@RequestParam("accept") boolean accept, @PathVariable String token) throws AlreadyJoinedException {
        teamService.processJoinTeam(token,accept);
        return new ResponseEntity<>("Thankyou",HttpStatus.OK);
    }
    @PutMapping("/invite")
    public ResponseEntity<Boolean> joinTeamFromInvite(@RequestParam("token") String token) throws AlreadyJoinedException {
        return new ResponseEntity<>(teamService.processJoinTeamByInvite(token),HttpStatus.OK);
    }


}
