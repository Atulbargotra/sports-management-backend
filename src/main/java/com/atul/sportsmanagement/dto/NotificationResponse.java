package com.atul.sportsmanagement.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class NotificationResponse {
    private Long id;
    private String message;
    private Instant date;
    private Long eventId;
    private String picture;
}
