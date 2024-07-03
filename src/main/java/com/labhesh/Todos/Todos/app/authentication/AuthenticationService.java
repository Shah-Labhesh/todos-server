package com.labhesh.Todos.Todos.app.authentication;

import com.labhesh.Todos.Todos.app.user.Users;
import com.labhesh.Todos.Todos.app.user.UsersRepo;
import com.labhesh.Todos.Todos.config.PasswordEncoder;
import com.labhesh.Todos.Todos.exception.BadRequestException;
import com.labhesh.Todos.Todos.exception.InternalServerException;
import com.labhesh.Todos.Todos.utils.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsersRepo usersRepo;
    private final MailService mailService;

    public ResponseEntity<?> registerUser(UserAuthenticateDto userAuthenticateDto) throws InternalServerException {
        try {
            System.out.println("userAuthenticateDto.getEmail() = " + userAuthenticateDto.getEmail());
            System.out.println("userAuthenticateDto.getPassword() = " + userAuthenticateDto.getPassword());

            Users user = Users.builder()
                    .email(userAuthenticateDto.getEmail())
                    .password(PasswordEncoder.encodePassword(userAuthenticateDto.getPassword()))
                    .verificationToken(UUID.randomUUID().toString())
                    .build();
            usersRepo.save(user);
            mailService.sendVerificationEmail(user);
            return ResponseEntity.created (null).body(new SuccessResponse("User registered successfully", user, null));
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Email already exists");
        } catch (Exception e) {
           throw  new InternalServerException("Something went wrong");
        }
    }

    public ResponseEntity<?> loginUser(UserAuthenticateDto userAuthenticateDto) throws InternalServerException, BadRequestException {
        try {
            Users user = usersRepo.findByEmail(userAuthenticateDto.getEmail()).orElseThrow(() -> new BadRequestException("Invalid credentials"));
            System.out.println(PasswordEncoder.matches(userAuthenticateDto.getPassword(), user.getPassword()));
            if (PasswordEncoder.matches(userAuthenticateDto.getPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().body("Invalid password");
            }
            if (!user.isVerified()) {

                user.setVerificationToken(UUID.randomUUID().toString());
                usersRepo.save(user);
                mailService.sendVerificationEmail(user);
                throw new BadRequestException("User not verified");
            }


            return ResponseEntity.ok(new SuccessResponse("User logged in successfully", user, null));
        }catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerException("Something went wrong");
        }
    }

    public boolean verifyUser(String token) {
        Optional<Users> userOptional = usersRepo.findByVerificationToken(token);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            user.setVerified(true);
            user.setVerificationToken(null);
            usersRepo.save(user);
            return true;
        }
        return false;
    }

    public ResponseEntity<?> resetPassword(String email) throws InternalServerException, BadRequestException {
        Optional<Users> userOptional = usersRepo.findByEmail(email);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            user.setResetPasswordToken(UUID.randomUUID().toString());
            usersRepo.save(user);
            mailService.sendResetPasswordEmail(user);
            return ResponseEntity.ok(new SuccessResponse("Reset password email sent", null, null));
        }
        throw new BadRequestException("User not found");
    }

    public boolean updatePassword(String token, String newPassword) {
        Optional<Users> userOptional = usersRepo.findByResetPasswordToken(token);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            user.setPassword(PasswordEncoder.encodePassword(newPassword)); // Add proper password encoding here
            user.setResetPasswordToken(null);
            usersRepo.save(user);
            return true;
        }
        return false;
    }

}
