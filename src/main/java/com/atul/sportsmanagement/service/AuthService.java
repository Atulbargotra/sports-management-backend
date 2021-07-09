package com.atul.sportsmanagement.service;

import com.atul.sportsmanagement.config.AppConfig;
import com.atul.sportsmanagement.dto.AuthenticationResponse;
import com.atul.sportsmanagement.dto.LoginRequest;
import com.atul.sportsmanagement.dto.RefreshTokenRequest;
import com.atul.sportsmanagement.dto.RegisterRequest;
import com.atul.sportsmanagement.exceptions.SpringSportsException;
import com.atul.sportsmanagement.model.NotificationEmail;
import com.atul.sportsmanagement.model.Role;
import com.atul.sportsmanagement.model.User;
import com.atul.sportsmanagement.model.VerificationToken;
import com.atul.sportsmanagement.repository.RoleRepository;
import com.atul.sportsmanagement.repository.UserRepository;
import com.atul.sportsmanagement.repository.VerificationTokenRepository;
import com.atul.sportsmanagement.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
@PropertySource("classpath:application.properties")
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final AppConfig appConfig;
    private static final String adminPassword = "verma";
    private final RoleRepository roleRepository;
    private MyUserDetailService userDetailService;

    @Transactional
    public void signup(RegisterRequest registerRequest,String roleName){
        if(userRepository.findByUsername(registerRequest.getUsername()).isPresent()){
            throw  new SpringSportsException("Username already exists");
        }
        else {
            Set<Role> roles = new HashSet<>();
            Role user_role = roleRepository.findByName(roleName);
            if(user_role == null){
                System.out.println("ininininn");
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
                roles.add(role);
            }
            roles.add(user_role);
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setCreated(Instant.now());
            user.setRoles(roles);
            user.setEnabled(false);
            userRepository.save(user);
            String token = generateVerificationToken(user);
            mailService.sendMail(new NotificationEmail("Please Activate your Account",
                    user.getEmail(), "Thank you for signing up , " +
                    "please click on the below url to activate your account : " +
                    appConfig.getUrl() + "/api/auth/accountVerification/" + token));
        }
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringSportsException("Invalid Token"));
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    public void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringSportsException("User not found with name "+username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        try{
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            final UserDetails userDetails = userDetailService
                    .loadUserByUsername(loginRequest.getUsername());
            System.out.println(userDetails.isEnabled());
            String token = jwtProvider.generateToken(userDetails);
            return AuthenticationResponse.builder()
                        .authenticationToken(token)
                        .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                        .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                        .username(loginRequest.getUsername())
                        .type(userDetails.getAuthorities().toString())
                        .build();

        }
        catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Bad Credentials");
        } catch (LockedException ex) {
            throw new LockedException("Account Locked");
        } catch (DisabledException ex) {
            throw new DisabledException("Account Not Activated");
        }
    }

    @Transactional
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

    public void signupAdmin(RegisterRequest registerRequest,String roleName) {
        if(registerRequest.getAdminAccessPassword().equals(adminPassword)){
            signup(registerRequest,roleName);
        }
        else{
            throw new SpringSportsException("Contact admin for password");
        }

    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }
}
