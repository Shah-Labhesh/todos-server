package com.labhesh.Todos.Todos.app.user;


import com.labhesh.Todos.Todos.exception.BadRequestException;
import com.labhesh.Todos.Todos.exception.InternalServerException;
import com.labhesh.Todos.Todos.utils.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UsersRepo usersRepo;
    private final ImageService imageService;

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
                user.setAvatarPath(imageService.saveImageToStorage(updateUserDto.getAvatar()));
                user.setAvatarMediaType(updateUserDto.getAvatar().getContentType());
            } catch (Exception e){
                throw new InternalServerException(e.getMessage());
            }
        }
        user.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        return ResponseEntity.ok(user);
    }
}
