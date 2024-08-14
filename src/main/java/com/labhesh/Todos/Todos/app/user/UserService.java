package com.labhesh.Todos.Todos.app.user;


import com.labhesh.Todos.Todos.app.files.Files;
import com.labhesh.Todos.Todos.app.files.FilesRepo;
import com.labhesh.Todos.Todos.exception.BadRequestException;
import com.labhesh.Todos.Todos.exception.InternalServerException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UsersRepo usersRepo;
    private final FilesRepo filesRepo;

    public ResponseEntity<?> getMe() throws BadRequestException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = usersRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<?> updateMe(UpdateUserDto updateUserDto) throws BadRequestException, InternalServerException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = usersRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
        if (!updateUserDto.getUsername().isEmpty()){
            user.setName(updateUserDto.getUsername());
        }
        if (!updateUserDto.getEmail().isEmpty()){
            user.setEmail(updateUserDto.getEmail());
        }
//        if (!updateUserDto.getPassword().isEmpty()){
//            user.setPassword(updateUserDto.getPassword());
//        }
        if (!updateUserDto.getAvatar().isEmpty()){
            try {
                if (user.getFile() != null){
                    filesRepo.delete(user.getFile());
                    Files files = Files.builder()
                            .fileName(updateUserDto.getAvatar().getOriginalFilename())
                            .fileType(updateUserDto.getAvatar().getContentType())
                            .data(updateUserDto.getAvatar().getBytes())
                            .build();
                    filesRepo.save(files);
                    user.setFile(files);
                }
            } catch (Exception e){
                throw new InternalServerException(e.getMessage());
            }
        }
        user.setUpdatedDate(LocalDateTime.now());
        return ResponseEntity.ok(user);
    }
}
