package com.atul.sportsmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class TeamMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String email;

    @JsonIgnore
    @ManyToMany(mappedBy = "members",fetch = FetchType.LAZY)
    private Set<Teams> myTeams = new HashSet<>();
}
