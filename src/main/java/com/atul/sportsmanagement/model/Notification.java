package com.atul.sportsmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long notificationId;

    private String message;

    private Instant createdAt;

    private boolean isRead;

    private Long eventId;

    private String picture;

    private String eventName;

    @ManyToOne
    private User user;

    public Notification(String message, Instant createdAt, User user) {
        this.user = user;
        this.createdAt = createdAt;
        this.isRead = false;
        this.message = message;
    }
    public Notification(String message, Instant createdAt,Long eventId,String picture,String eventName) {
        this.createdAt = createdAt;
        this.isRead = false;
        this.message = message;
        this.eventId = eventId;
        this.picture = picture;
        this.eventName = eventName;
    }
}
