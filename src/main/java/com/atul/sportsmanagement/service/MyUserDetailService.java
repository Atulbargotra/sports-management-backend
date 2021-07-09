package com.atul.sportsmanagement.service;

import com.atul.sportsmanagement.model.User;
import com.atul.sportsmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("Username not found "+userName));
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),user.isEnabled(),true,true,true, getAuthority(user));
    }

    private Set getAuthority(User user) {
        Set authorities = new HashSet();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });
        return authorities;
    }
}
