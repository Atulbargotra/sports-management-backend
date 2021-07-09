package com.atul.sportsmanagement.dto;

import lombok.Data;

@Data
public class TeamResponse {
    private Long id;
    private String name;
    private String description;
    private String city;
    private Integer currentNumberOfMembers;
    private String email;
}
