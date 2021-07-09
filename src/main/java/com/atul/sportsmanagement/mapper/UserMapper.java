package com.atul.sportsmanagement.mapper;

import com.atul.sportsmanagement.dto.UpdateUserProfileRequest;
import com.atul.sportsmanagement.dto.UserProfileResponse;
import com.atul.sportsmanagement.dto.UserResponse;
import com.atul.sportsmanagement.model.TeamMembers;
import com.atul.sportsmanagement.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public TeamMembers mapUserToTeamMember(User user){
        TeamMembers teamMembers = new TeamMembers();
        teamMembers.setEmail(user.getEmail());
        return teamMembers;
    }
    public TeamMembers mapStringEmailMembersToTeamMembersObject(String email){
        TeamMembers member = new TeamMembers();
        member.setEmail(email);
        return member;
    }
    public User map(UpdateUserProfileRequest request, User user){
        user.setPicture(request.getPicture());
        user.setFirstName(request.getFirstName());
        user.setAddress(request.getAddress());
        user.setCity(request.getCity());
        user.setPicture(request.getPicture());
        user.setContact(request.getContact());
        user.setGender(request.getGender());
        user.setLastName(request.getLastName());
        return user;
    }
    public UserProfileResponse mapToDto(User user){
        UserProfileResponse response = new UserProfileResponse();
        response.setPicture(user.getPicture());
        response.setFirstName(user.getFirstName());
        response.setAddress(user.getAddress());
        response.setCity(user.getCity());
        response.setPicture(user.getPicture());
        response.setContact(user.getContact());
        response.setGender(user.getGender());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        return response;
    }
    public UserResponse mapToUserResponse(User user){
        UserResponse response = new UserResponse();
        response.setName(user.getUsername());
        response.setPicture(user.getPicture());
        response.setEmail(user.getEmail());
        return response;
    }

}
