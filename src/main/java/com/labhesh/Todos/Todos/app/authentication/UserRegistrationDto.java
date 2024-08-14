package com.labhesh.Todos.Todos.app.authentication;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {

    @NotBlank(message = "username cannot be empty")
    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Name must not contain special characters and must be at least 3 characters long")
    private String username;

    @NotBlank(message = "email cannot be empty")
    @Email(message = "email should be valid")
    private String email;
    @NotBlank(message = "password cannot be empty")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must contain at least 8 characters, one uppercase, one lowercase, one number and one special character")
    private String password;
}
