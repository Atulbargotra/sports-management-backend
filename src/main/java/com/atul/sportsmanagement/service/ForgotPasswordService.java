package com.atul.sportsmanagement.service;

import com.atul.sportsmanagement.config.AppConfig;
import com.atul.sportsmanagement.model.NotificationEmail;
import com.atul.sportsmanagement.model.User;
import com.atul.sportsmanagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class ForgotPasswordService {
    private final UserRepository userRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final AppConfig appConfig;
    public String  processForgotPassword(String email,Model model) {
        User user;
        if(userRepository.findByEmail(email).isPresent()){
            user = userRepository.findByEmail(email).get();
            UUID token = UUID.randomUUID();
            user.setResetPasswordToken(token.toString());
            mailService.sendMail(new NotificationEmail("Here's the link to reset your password!",
                    user.getEmail(), "You have requested to reset your password., " +
                    "Click the link below to change your password:" +
                    appConfig.getUrl()+"/reset_password/?token=" + token));
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
            return "forgot_password_form";
        }
        else {
            model.addAttribute("error","No user present with this email");
            return "forgot_password_form";
        }

    }
    public void updatePassword(String password, User user){
        user.setPassword(passwordEncoder.encode(password));
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    public String processResetPassword(String token, String newPassword, Model model) {
        User user;
        if(userRepository.findByResetPasswordToken(token).isPresent()){
            user = userRepository.findByResetPasswordToken(token).get();
            updatePassword(newPassword, user);
            model.addAttribute("message", "You have successfully changed your password.");
        }
       else{
            model.addAttribute("message", "Invalid Token");
            return "message";
        }
        return "message";
    }
}
