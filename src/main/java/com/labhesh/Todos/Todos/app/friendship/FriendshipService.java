package com.labhesh.Todos.Todos.app.friendship;

import com.labhesh.Todos.Todos.app.user.Users;
import com.labhesh.Todos.Todos.app.user.UsersRepo;
import com.labhesh.Todos.Todos.exception.BadRequestException;
import com.labhesh.Todos.Todos.utils.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public ResponseEntity<?> acceptFriendRequest(UUID userId, UUID friendId, Boolean value) throws BadRequestException {
        Optional<FriendShip> optionalFriendship = friendshipRepo.findByUserIdAndFriendId(userId, friendId);
        if (optionalFriendship.isPresent()) {
            FriendShip friendship = optionalFriendship.get();
            if (!value){
                friendshipRepo.delete(friendship);
                return ResponseEntity.ok(new SuccessResponse("Friend request rejected", friendship, null));
            }
            friendship.setAccepted(true);
            friendshipRepo.save(friendship);
            return ResponseEntity.ok(new SuccessResponse("Friend request accepted", friendship, null));
        } else {
            throw new BadRequestException("Friend request not found");
        }
    }

    // teamlead, member
    public boolean areFriends(UUID userId, UUID friendId) {
System.out.println(userId);
System.out.println(friendId);
        System.out.println(friendshipRepo.findByUserIdAndFriendIdAndAcceptedIsTrue(userId, friendId).isEmpty());
        System.out.println(friendshipRepo.findByUserIdAndFriendIdAndAcceptedIsTrue(friendId, userId).isPresent());
        if ( friendshipRepo.findByUserIdAndFriendIdAndAcceptedIsTrue(userId, friendId).isEmpty()) {
            return friendshipRepo.findByUserIdAndFriendIdAndAcceptedIsTrue(friendId, userId).isPresent();
        }else{
            return true;
        }
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
        List<FriendShip> friends = friendshipRepo.findAllMyFriend(userId,userId);
        List<Users> otherUsers = new ArrayList<>();
        // check i am a user or friend if friend then add to user to list else
        if (!friends.isEmpty()) {
            friends.forEach(friend -> {
                if (friend.getUser().getId().equals(userId)) {
                    otherUsers.add(friend.getFriend());
                } else {
                    otherUsers.add(friend.getUser());
                }
            });
        }
        // remove friends from all users
        List<Users> users = usersRepo.findAll().stream().filter(u -> !otherUsers.contains(u)).collect(Collectors.toList());
        users.remove(user);
        return ResponseEntity.ok(users);
    }
}
