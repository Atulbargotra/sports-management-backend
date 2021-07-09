package com.atul.sportsmanagement.controller;

import com.atul.sportsmanagement.service.ForgotPasswordService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@AllArgsConstructor
public class ForgotPasswordController {
    private final ForgotPasswordService forgotPasswordService;

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model){
        String email = request.getParameter("email");
        return forgotPasswordService.processForgotPassword(email,model);

    }

    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        model.addAttribute("token", token);
        return "reset_password_form";
    }
    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model){
        String token = request.getParameter("token");
        String newPassword = request.getParameter("password");
        return forgotPasswordService.processResetPassword(token,newPassword,model);
    }
}
