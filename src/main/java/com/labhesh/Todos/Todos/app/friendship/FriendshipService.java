package com.labhesh.Todos.Todos.app.friendship;

import com.labhesh.Todos.Todos.app.user.Users;
import com.labhesh.Todos.Todos.app.user.UsersRepo;
import com.labhesh.Todos.Todos.exception.BadRequestException;
import com.labhesh.Todos.Todos.utils.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendshipService {

    private final FriendshipRepo friendshipRepo;
    private final UsersRepo usersRepo;


    public ResponseEntity<?> sendFriendRequest(UUID userId, UUID friendId) throws BadRequestException {
        Users user = usersRepo.findById(userId).orElseThrow(() -> new BadRequestException("User not found"));
        Users friend = usersRepo.findById(friendId).orElseThrow(() -> new BadRequestException("Friend not found"));

        FriendShip friendship = FriendShip.builder()
                .user(user)
                .friend(friend)
                .sentBy(userId)
                .build();

        friendshipRepo.save(friendship);
        return ResponseEntity.ok(new SuccessResponse("Friend request sent", friendship, null));
    }

    public ResponseEntity<?> acceptFriendRequest(UUID userId, UUID friendId) throws BadRequestException {
        Optional<FriendShip> optionalFriendship = friendshipRepo.findByUserIdAndFriendId(userId, friendId);
        if (optionalFriendship.isPresent()) {
            FriendShip friendship = optionalFriendship.get();
            friendship.setAccepted(true);
            friendshipRepo.save(friendship);
            return ResponseEntity.ok(new SuccessResponse("Friend request accepted", friendship, null));
        } else {
            throw new BadRequestException("Friend request not found");
        }
    }

    public boolean areFriends(UUID userId, UUID friendId) {
        return friendshipRepo.findByUserIdAndFriendIdAndAcceptedIsTrue(userId, friendId).isEmpty();
    }

    // all friends of a user
    public ResponseEntity<?> getFriends(UUID userId) throws BadRequestException {
        Users user = usersRepo.findById(userId).orElseThrow(() -> new BadRequestException("User not found"));
        List<FriendShip> friends =  friendshipRepo.findAllByUserAndAcceptedIsTrue(user);
        return ResponseEntity.ok(friends);
    }

    // cancel friend request
    public ResponseEntity<?> cancelFriendRequest(UUID userId, UUID friendId) throws BadRequestException {
        Optional<FriendShip> optionalFriendship = friendshipRepo.findByUserIdAndFriendId(userId, friendId);

        if (optionalFriendship.isEmpty()) {
            throw new BadRequestException("Friend request not found");
        }
        if (!optionalFriendship.get().getSentBy().equals(userId)){
            throw new BadRequestException("You are not authorized to cancel this request");
        }
        if (optionalFriendship.get().isAccepted()) {
            throw new BadRequestException("Friend request already accepted");
        }
        friendshipRepo.delete(optionalFriendship.get());
        return ResponseEntity.ok(new SuccessResponse("Friend request cancelled", optionalFriendship, null));
    }

    // unfriend
    public ResponseEntity<?> unfriend(UUID userId, UUID friendId) throws BadRequestException {
        Optional<FriendShip> optionalFriendship = friendshipRepo.findByUserIdAndFriendId(userId, friendId);
        if (optionalFriendship.isEmpty()) {
            throw new BadRequestException("Friend request not found");
        }
        if (!optionalFriendship.get().isAccepted()) {
            throw new BadRequestException("Friend request not accepted");
        }

        friendshipRepo.delete(optionalFriendship.get());
        return ResponseEntity.ok(new SuccessResponse("Unfriend successfully", optionalFriendship, null));

    }

    // get friend requests
    public ResponseEntity<?> getFriendRequests(UUID userId) {
        List<FriendShip> friendRequests = friendshipRepo.findAllByFriendIdAndAcceptedIsFalse(userId);
        return ResponseEntity.ok(friendRequests);
    }

    // get all users except friends and self
    public ResponseEntity<?> getAllUsersExceptFriends(UUID userId) throws BadRequestException {
        Users user = usersRepo.findById(userId).orElseThrow(() -> new BadRequestException("User not found"));
        List<Users> friends = friendshipRepo.findAllByUserAndAcceptedIsTrue(user)
                .stream()
                .map(FriendShip::getFriend)
                .collect(Collectors.toList());
        friends.add(user);
        return ResponseEntity.ok(usersRepo.findAll().stream().filter(u -> !friends.contains(u)).collect(Collectors.toList()));
    }
}
