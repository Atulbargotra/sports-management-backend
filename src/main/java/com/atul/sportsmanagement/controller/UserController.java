package com.atul.sportsmanagement.controller;

import com.atul.sportsmanagement.dto.UpdateUserProfileRequest;
import com.atul.sportsmanagement.dto.UserProfileResponse;
import com.atul.sportsmanagement.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/user")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    @PatchMapping
    public ResponseEntity<Void> updateProfile(@RequestBody UpdateUserProfileRequest request){
        userService.updateProfile(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<UserProfileResponse> getProfile(){
        return new ResponseEntity<>(userService.getProfile(),HttpStatus.OK);
    }


}
