package com.atul.sportsmanagement.dto;

import lombok.Data  ;

@Data
public class EventResponse {
    private Long id;
    private String eventName;
    private String description;
    private String category;
    private String type;
    private Long maxParticipant;
    private Integer totalRegistered;
    private String lastDate;
    private String eventDate;
    private String location;
    private String venue;
    private Integer maxMembersInTeam;
    private String picture;
    private String w1;
    private String w2;
    private String w3;
}
