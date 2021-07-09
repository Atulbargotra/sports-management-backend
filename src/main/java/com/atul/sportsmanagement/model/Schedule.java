package com.atul.sportsmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long eventId;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,targetEntity = Match.class)
    @JoinColumn(name = "sm_fk",referencedColumnName = "id")
    private List<Match> matches = new ArrayList<>();

}

