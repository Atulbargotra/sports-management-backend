package com.atul.sportsmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {
    private Long eventId;
    private String eventName;
    private String description;
    private Long maxParticipant;
    private String location;
    private String category;
    private String type;
    private String eventDate;
    private String lastDate;
    private String picture;
    private Integer maxMembersInTeam;
    private String venue;
}
