package com.atul.sportsmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity


public class Teams {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long teamId;
    @Column(nullable = false,unique = true)
    private String name;
    @Nullable
    private String description;
    private String email;
    private String city;
    private Integer maxMember;
    private String contact;
    private String inviteLink;
    @ManyToMany(fetch = LAZY)
    @JoinTable(name = "members",
            joinColumns = @JoinColumn(name = "TEAM_ID", referencedColumnName = "teamId"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "userId"))
    private Set<User> members = new HashSet<>();
    @Column(nullable = false)
    private Long eventId;
    @JsonIgnore
    @ManyToMany(mappedBy = "teamsParticipated", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Event> eventsImRegistered = new HashSet<>();
    private Long adminUserId;

}
