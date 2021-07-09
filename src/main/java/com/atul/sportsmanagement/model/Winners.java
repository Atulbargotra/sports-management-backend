package com.atul.sportsmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Winners {
    @Id
    private Long eventId;

    @NotBlank(message = "Winner name cannot be blank or Null")
    private String winner;

    @Nullable
    private String firstRunnerUp;

    @Nullable
    private String secondRunnerUp;

    @OneToOne
    @MapsId
    @JoinColumn(name = "eventId")
    private Event event;
}
