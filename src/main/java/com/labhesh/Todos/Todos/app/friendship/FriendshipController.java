package com.labhesh.Todos.Todos.app.friendship;


import com.labhesh.Todos.Todos.exception.BadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/friendship")
@RequiredArgsConstructor
@Tag(name = "Friendship", description = "Friendship related endpoints")
public class FriendshipController {

    private final FriendshipService friendshipService;

    @Operation(summary = "Send a friend request", description = "Send a friend request to another user")
    @PostMapping("/send-request")
    public ResponseEntity<?> sendFriendRequest(@RequestParam UUID userId, @RequestParam UUID friendId) throws BadRequestException {
        return friendshipService.sendFriendRequest(userId, friendId);
    }

    @Operation(summary = "Accept a friend request", description = "Accept a friend request from another user")
    @PostMapping("/accept-request")
    public ResponseEntity<?> acceptFriendRequest(@RequestParam UUID userId, @RequestParam UUID friendId, @RequestParam Boolean value) throws BadRequestException {
        return friendshipService.acceptFriendRequest(userId, friendId, value);
    }

    @Operation(summary = "Get all friends of a user", description = "Get all friends of a user")
    @GetMapping("/friends")
    public ResponseEntity<?> getFriends(@RequestParam UUID userId) throws BadRequestException {
        return friendshipService.getFriends(userId);
    }

//    @Operation(summary = "Check if two users are friends", description = "Check if two users are friends")
//
//    @PostMapping("/are-friends")
//    public ResponseEntity<?> areFriends(@RequestParam UUID userId, @RequestParam UUID friendId) {
//        return friendshipService.areFriends(userId, friendId);
//    }

    @Operation(summary = "Get all friend requests of a user", description = "Get all friend requests of a user")
    @GetMapping("/friend-requests")
    public ResponseEntity<?> getFriendRequests(@RequestParam UUID userId) {
        return friendshipService.getFriendRequests(userId);
    }

    // unfriend
    @Operation(summary = "Unfriend a user", description = "Unfriend a user")
    @PostMapping("/unfriend")
    public ResponseEntity<?> unfriend(@RequestParam UUID userId, @RequestParam UUID friendId) throws BadRequestException {
        return friendshipService.unfriend(userId, friendId);
    }

    // cancel friend request
    @Operation(summary = "Cancel a friend request", description = "Cancel a friend request")
    @PostMapping("/cancel-request")
    public ResponseEntity<?> cancelRequest(@RequestParam UUID userId, @RequestParam UUID friendId) throws BadRequestException {
        return friendshipService.cancelFriendRequest(userId, friendId);
    }

    @Operation(summary = "Get all users except friends", description = "Get all users except friends")
    @GetMapping("other-users/{userId}")
    public ResponseEntity<?> getUsers(@PathVariable UUID userId) throws BadRequestException {
        return friendshipService.getAllUsersExceptFriends(userId);
    }
}
