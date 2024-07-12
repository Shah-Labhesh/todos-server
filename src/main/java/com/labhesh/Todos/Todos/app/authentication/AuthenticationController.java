package com.labhesh.Todos.Todos.app.authentication;

import com.labhesh.Todos.Todos.exception.BadRequestException;
import com.labhesh.Todos.Todos.exception.InternalServerException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;


@Tag(name = "Authentication", description = "Endpoints for user authentication")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Login user")
    @PostMapping("/login")

    public ResponseEntity<?> login(@RequestBody @Valid UserAuthenticateDto userAuthenticateDto) throws InternalServerException, BadRequestException {
        return authenticationService.loginUser(userAuthenticateDto);
    }

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public CompletableFuture<ResponseEntity<?>> register(@RequestBody @Valid UserRegistrationDto userRegisterDto) throws InternalServerException {
        return authenticationService.registerUser(userRegisterDto);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> resetPassword(@RequestParam("email") String email) throws InternalServerException, BadRequestException {
       return authenticationService.resetPassword(email);
    }

    @PostMapping("upload-avatar/{userId}")
    public ResponseEntity<?> uploadAvatar(@PathVariable("userId") String userId, @ModelAttribute("avatar") MultipartFile avatar) throws InternalServerException, BadRequestException {
        return authenticationService.uploadAvatar(userId,avatar);
    }
}
