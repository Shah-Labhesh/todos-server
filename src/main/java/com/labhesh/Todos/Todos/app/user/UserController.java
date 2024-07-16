package com.labhesh.Todos.Todos.app.user;

import com.labhesh.Todos.Todos.app.files.Files;
import com.labhesh.Todos.Todos.app.files.FilesRepo;
import com.labhesh.Todos.Todos.exception.BadRequestException;
import com.labhesh.Todos.Todos.exception.InternalServerException;
import com.labhesh.Todos.Todos.utils.ImageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints for User")
public class UserController {

    private final ImageService imageService;
    private final FilesRepo filesRepo;
    private final UserService userService;

    @GetMapping("/avatar/{avatar}")
    public ResponseEntity<?> getAvatar(@PathVariable("avatar") String avatar) throws BadRequestException, InternalServerException {

        Files file = filesRepo.findById(UUID.fromString(avatar)).orElseThrow(
                () -> new BadRequestException("Image not found")
        );

        try {
            return ResponseEntity.ok().contentType(imageService.getImageContentType(file.getFileType()))
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                    .body(file.getData());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    @SecurityRequirement(name = "auth")
    @GetMapping
    public ResponseEntity<?> getMe() throws BadRequestException {
        return userService.getMe();
    }

    @SecurityRequirement(name = "auth")
    @PutMapping
    public ResponseEntity<?> updateMe(@RequestBody @Valid @ModelAttribute UpdateUserDto updateUserDto) throws BadRequestException, InternalServerException {
        return userService.updateMe(updateUserDto);
    }


}
