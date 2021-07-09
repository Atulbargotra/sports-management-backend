package com.atul.sportsmanagement.model;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "event_published")
public class Event {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long eventId;
    @NotBlank(message = "Event name cannot be empty or Null")
    private String eventName;
    @Nullable
    @Lob
    private String description;
    private Long maxParticipant = 100L;
    @NotBlank(message = "Location cannot be empty or Null")
    private String location;
    @NotBlank(message = "Category cannot be empty or Null")
    private String category;
    @Enumerated(EnumType.STRING)
    private EventType type;
    private Boolean isPublished = false;
    private Instant createdDate;
    private Instant eventDate;
    private String picture;
    private Integer maxMembersInTeam;
    private String venue;
    private Instant lastDate;
    @ManyToMany(fetch = LAZY)
    @JoinTable(name = "participants_in_event",
            joinColumns = @JoinColumn(name = "EVENT_ID", referencedColumnName = "eventId"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "userId"))
    private Set<User> participants = new HashSet<>();

    @ManyToMany(fetch = LAZY,cascade = CascadeType.REMOVE)
    @JoinTable(name = "teams_in_event",
            joinColumns = @JoinColumn(name = "EVENT_ID", referencedColumnName = "eventId"),
            inverseJoinColumns = @JoinColumn(name = "TEAM_ID", referencedColumnName = "teamId"))
    private Set<Teams> teamsParticipated = new HashSet<>();

    @OneToOne(fetch = LAZY,cascade = CascadeType.ALL, mappedBy = "event")
    @PrimaryKeyJoinColumn
    private Winners winners;
}
