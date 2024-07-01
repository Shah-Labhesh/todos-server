package com.labhesh.Todos.Todos.app.authentication;

import com.labhesh.Todos.Todos.app.user.Users;
import com.labhesh.Todos.Todos.app.user.UsersRepo;
import com.labhesh.Todos.Todos.config.PasswordEncoder;
import com.labhesh.Todos.Todos.exception.BadRequestException;
import com.labhesh.Todos.Todos.exception.InternalServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsersRepo usersRepo;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> registerUser(UserAuthenticateDto userAuthenticateDto) throws InternalServerException {
        try {
            Users user = Users.builder()
                    .email(userAuthenticateDto.getEmail())
                    .password(PasswordEncoder.encodePassword(userAuthenticateDto.getPassword()))
                    .build();
            usersRepo.save(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Email already exists");
        }
        catch (Exception e) {
           throw  new InternalServerException("Something went wrong");
        }
    }

    public ResponseEntity<?> loginUser(UserAuthenticateDto userAuthenticateDto) throws InternalServerException {
        try {
            Users user = usersRepo.findByEmail(userAuthenticateDto.getEmail()).orElseThrow(() -> new BadRequestException("Invalid credentials"));

            if (!user.getPassword().equals(userAuthenticateDto.getPassword())) {
                return ResponseEntity.badRequest().body("Invalid password");
            }
            // map
            HashMap<String, Object> map = new HashMap<>();
            map.put("message", "User logged in successfully");
            map.put("user", user);

            return ResponseEntity.ok(map);
        } catch (Exception e) {
            throw new InternalServerException("Something went wrong");
        }
    }
}
