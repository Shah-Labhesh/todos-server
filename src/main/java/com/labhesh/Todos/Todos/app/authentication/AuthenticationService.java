package com.labhesh.Todos.Todos.app.authentication;

import com.labhesh.Todos.Todos.app.files.Files;
import com.labhesh.Todos.Todos.app.files.FilesRepo;
import com.labhesh.Todos.Todos.app.user.Users;
import com.labhesh.Todos.Todos.app.user.UsersRepo;
import com.labhesh.Todos.Todos.config.JwtTokenUtil;
import com.labhesh.Todos.Todos.config.PasswordEncoder;
import com.labhesh.Todos.Todos.exception.BadRequestException;
import com.labhesh.Todos.Todos.exception.InternalServerException;
import com.labhesh.Todos.Todos.utils.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@EnableAsync
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsersRepo usersRepo;
    private final FilesRepo filesRepo;
    private final MailService mailService;
    private final JwtTokenUtil util;

    @Async
    public CompletableFuture<ResponseEntity<?>> registerUser(UserRegistrationDto userRegistrationDto) throws InternalServerException {
        try {
            System.out.println("userAuthenticateDto.getEmail() = " + userRegistrationDto.getEmail());
            System.out.println("userAuthenticateDto.getPassword() = " + userRegistrationDto.getPassword());

            Users user = Users.builder()
                    .name(userRegistrationDto.getUsername())
                    .email(userRegistrationDto.getEmail())
                    .password(PasswordEncoder.encodePassword(userRegistrationDto.getPassword()))
                    .verificationToken(UUID.randomUUID().toString())
                    .build();
            usersRepo.save(user);
            mailService.sendVerificationEmail(user);
            return CompletableFuture.completedFuture(ResponseEntity.created(null).body(new SuccessResponse("User registered successfully", user, null)));
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
                throw new BadRequestException("Invalid password");
            }
            if (!user.isVerified()) {

                user.setVerificationToken(UUID.randomUUID().toString());
                usersRepo.save(user);
                mailService.sendVerificationEmail(user);
                throw new BadRequestException("User not verified");
            }
            HashMap<String, Object> response = new HashMap<>();
            response.put("message", "User logged in successfully");
            response.put("token", util.generateToken(user));
            response.put("data", user);

            return ResponseEntity.ok(response);
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
        System.out.println("token = " + token);
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

    public ResponseEntity<?> uploadAvatar(String userId, MultipartFile avatar) throws BadRequestException, InternalServerException {
        Users user = usersRepo.findById(UUID.fromString(userId)).orElseThrow(() -> new BadRequestException("User not found"));
        try{
            if (!avatar.isEmpty()){
                Files files = Files.builder()
                        .fileName(avatar.getOriginalFilename())
                        .fileType(avatar.getContentType())
                        .data(avatar.getBytes())
                        .build();
                filesRepo.save(files);
                user.setFile(files);
                usersRepo.save(user);
            }

            return ResponseEntity.ok(new SuccessResponse("Image Uploaded successfully", user, null));
        }
        catch (Exception e){
            throw new InternalServerException(e.getMessage());
        }
    }

    public UserDetails loadUserByUsername(String username) throws BadRequestException {
        Users user = usersRepo.findByEmail(username).orElseThrow(() -> new BadRequestException("User not found"));
        if (user != null) {
            if (!user.isVerified()){
                throw new UsernameNotFoundException("User is not verified");
            }
            return user;
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
    }
}
