package com.labhesh.Todos.Todos.app.authentication;

import com.labhesh.Todos.Todos.app.user.Users;
import com.labhesh.Todos.Todos.app.user.UsersRepo;
import com.labhesh.Todos.Todos.exception.BadRequestException;
import com.labhesh.Todos.Todos.exception.InternalServerException;
import com.labhesh.Todos.Todos.utils.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class WebController {


    private final AuthenticationService authenticationService;
    private final ImageService imageService;
    private final UsersRepo usersRepo;

    @GetMapping("/verify")
    public String verifyUser(@RequestParam("token") String token) {
        if (authenticationService.verifyUser(token)) {
            return "verification_success";
        } else {
            return "verification_fail";
        }
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

    @GetMapping("/{avatar}")
    public ResponseEntity<?> getAvatar(@PathVariable("avatar") String avatar) throws BadRequestException, InternalServerException {

        Users user = usersRepo.findByAvatarPath(avatar).orElseThrow(
                () -> new BadRequestException("Image not found")
        );

        try {
           return ResponseEntity.ok().contentType(imageService.getImageContentType(user.getAvatarMediaType())).body(imageService.getImage(avatar));
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    @GetMapping("/")
    public String home() {
        return "home";

    }
}
