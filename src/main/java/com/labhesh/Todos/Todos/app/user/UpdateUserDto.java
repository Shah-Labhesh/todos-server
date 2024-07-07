package com.labhesh.Todos.Todos.app.user;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {

    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Name must not contain special characters and must be at least 3 characters long")
    private String username;

    @Email(message = "email should be valid")
    private String email;
//    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must contain at least 8 characters, one uppercase, one lowercase, one number and one special character")
//    private String password;

    private MultipartFile avatar;
}
