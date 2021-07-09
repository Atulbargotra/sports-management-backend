package com.atul.sportsmanagement.service;

import com.atul.sportsmanagement.dto.UpdateUserProfileRequest;
import com.atul.sportsmanagement.dto.UserProfileResponse;
import com.atul.sportsmanagement.mapper.UserMapper;
import com.atul.sportsmanagement.model.User;
import com.atul.sportsmanagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthService authService;
    public void updateProfile(UpdateUserProfileRequest request){
        User user = userMapper.map(request,authService.getCurrentUser());
        userRepository.save(user);
    }
    public UserProfileResponse getProfile(){
        User user = authService.getCurrentUser();
        return userMapper.mapToDto(user);
    }
}
