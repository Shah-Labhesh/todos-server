package com.labhesh.Todos.Todos.app.user;

import com.labhesh.Todos.Todos.exception.BadRequestException;
import com.labhesh.Todos.Todos.exception.InternalServerException;
import com.labhesh.Todos.Todos.utils.ImageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints for User")
public class UserController {

    private final ImageService imageService;
    private final UsersRepo usersRepo;

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
}
