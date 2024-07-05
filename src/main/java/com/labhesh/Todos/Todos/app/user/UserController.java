package com.labhesh.Todos.Todos.app.user;

import com.labhesh.Todos.Todos.exception.BadRequestException;
import com.labhesh.Todos.Todos.exception.InternalServerException;
import com.labhesh.Todos.Todos.utils.ImageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints for User")
public class UserController {

    private final ImageService imageService;
    private final UsersRepo usersRepo;
    private final UserService userService;

    @GetMapping("/avatar/{avatar}")
    public ResponseEntity<?> getAvatar(@PathVariable("avatar") String avatar) throws BadRequestException, InternalServerException {

        Users user = usersRepo.findByAvatarPath(avatar).orElseThrow(
                () -> new BadRequestException("Image not found")
        );

        try {
            return ResponseEntity.ok().contentType(imageService.getImageContentType(user.getAvatarMediaType())).body(imageService.getImage(avatar));
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
