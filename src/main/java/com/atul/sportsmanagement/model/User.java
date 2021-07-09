package com.atul.sportsmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "\"User\"")
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long userId;

    @NotBlank(message = "Username is required")
    @Column(unique = true, length = 25,nullable = false)
    private String username;
    private String firstName;
    private String lastName;
    private String gender;
    private String city;
    private String address;
    private String contact;
    private String picture;

    @Column(length = 100)
    @Size(min = 3,max = 100)
    @NotBlank(message = "Password is required")
    private String password;

    @Email
    @NotEmpty(message = "Email is required")
    @Column(nullable = false,unique = true)
    private String email;

    private Instant created;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "USER_ROLES", joinColumns = {
            @JoinColumn(name = "USER_ID",referencedColumnName = "userId") }, inverseJoinColumns = {
            @JoinColumn(name = "ROLE_ID",referencedColumnName = "id") })
    private Set<Role> roles;

    @JsonIgnore
    @ManyToMany(mappedBy = "participants", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Event> eventsImAttending = new HashSet<>();

//    @JsonIgnore
//    @ManyToMany(mappedBy = "members",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
//    private Set<Teams> myTeams = new HashSet<>();

    @Nullable
    private String resetPasswordToken;

    private boolean enabled = false;

}
