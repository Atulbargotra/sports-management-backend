package com.atul.sportsmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserProfileRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String gender;
    private String city;
    private String address;
    private String contact;
    private String picture;
}
