package com.labhesh.Todos.Todos.app.authentication;

import com.labhesh.Todos.Todos.exception.BadRequestException;
import com.labhesh.Todos.Todos.exception.InternalServerException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.validation.Valid;

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
    public ResponseEntity<?> register(@RequestBody @Valid UserAuthenticateDto userRegisterDto) throws InternalServerException {
        return authenticationService.registerUser(userRegisterDto);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> resetPassword(@RequestParam("email") String email) throws InternalServerException, BadRequestException {
       return authenticationService.resetPassword(email);
    }

}
