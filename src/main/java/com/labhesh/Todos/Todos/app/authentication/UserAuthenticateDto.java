package com.labhesh.Todos.Todos.app.authentication;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthenticateDto {

    @NotBlank(message = "email cannot be empty")
    @Email(message = "email should be valid")
    private String email;
    @NotBlank(message = "password cannot be empty")
    @Size(min = 8 , message = "password should be at least 8 characters long")
    private String password;
}
