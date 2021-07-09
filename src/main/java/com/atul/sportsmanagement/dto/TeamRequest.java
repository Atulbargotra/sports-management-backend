package com.atul.sportsmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamRequest {
    private String name;
    private String description;
    private String city;
    private Integer maxMembers;
    private String contact;
    private Long eventId;
}
