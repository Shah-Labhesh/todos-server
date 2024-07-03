package com.labhesh.Todos.Todos.app.authentication;

import com.labhesh.Todos.Todos.exception.InternalServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final AuthenticationService authenticationService;


    @GetMapping("/verify")
    public String verifyUser(@RequestParam("token") String token) {
        if (authenticationService.verifyUser(token)) {
            return "verification_success";
        } else {
            return "verification_fail";
        }
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("email") String email) throws InternalServerException {
        authenticationService.resetPassword(email);
        return "reset_password_email_sent";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "reset_password_form";
    }

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam("token") String token, @RequestParam("password") String newPassword) {
        if (authenticationService.updatePassword(token, newPassword)) {
            return "update_password_success";
        } else {
            return "update_password_fail";
        }
    }
}
