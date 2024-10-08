package com.labhesh.Todos.Todos.app.messaging;

import com.labhesh.Todos.Todos.app.friendship.FriendshipService;
import com.labhesh.Todos.Todos.app.user.Users;
import com.labhesh.Todos.Todos.app.user.UsersRepo;
import com.labhesh.Todos.Todos.config.JwtTokenUtil;
import com.labhesh.Todos.Todos.exception.BadRequestException;
import com.labhesh.Todos.Todos.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {

    private final UsersRepo userRepo;
    private final JwtTokenUtil jwtHelper;
    private final ChatRoomRepo chatRoomRepo;
    private final MessageRepo messageRepo;
    private final FriendshipService friendshipService;


    public ResponseEntity<?> getChatRoom(String token) throws BadRequestException {
        String email = jwtHelper.getUsernameFromToken(token);
        Users users = userRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
        return ResponseEntity.ok(chatRoomRepo.findAllByUserOrderByLastMessageTimeDesc(users));
    }

    public void createMessage(UUID roomId, String token, String message) throws BadRequestException, ForbiddenException {
        String email = jwtHelper.getUsernameFromToken(token);
        Users users = userRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
        ChatRooms chatRooms = chatRoomRepo.findById(roomId).orElseThrow(() -> new BadRequestException("Room not found"));
        if (!chatRooms.getUsers().contains(users)) {
            throw new ForbiddenException("You are not allowed to send message in this room");
        }
        Message messages = Message.builder()
                .chatRoom(chatRooms)
                .message(message)
                .sender(users)
                .build();
        messageRepo.save(messages);

        chatRooms.setLastMessage(message);
        chatRooms.setLastMessageTime(messages.getSentAt());
        chatRooms.setLastMessageBy(users.getId());
        chatRoomRepo.save(chatRooms);
    }

    public List<Message> getRoomMessages(UUID roomId, String token) throws BadRequestException, ForbiddenException {
        String email = jwtHelper.getUsernameFromToken(token);
        Users users = userRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
        ChatRooms chatRooms = chatRoomRepo.findById(roomId).orElseThrow(() -> new BadRequestException("Room not found"));
        if (!chatRooms.getUsers().contains(users)) {
            throw new ForbiddenException("You are not allowed to send message in this room");
        }
        return messageRepo.findAllByChatRoom(chatRooms);
    }

    public void deleteMessage(UUID messageId, String token) throws BadRequestException, ForbiddenException {
        String email = jwtHelper.getUsernameFromToken(token);
        Users users = userRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
        Message message = messageRepo.findById(messageId).orElseThrow(() -> new BadRequestException("Message not found"));
        if (!message.getSender().equals(users)) {
            throw new ForbiddenException("You are not allowed to send message in this room");
        }
        messageRepo.delete(message);
    }

    public String createChatRoom(CreateChatRoomDto dto) throws BadRequestException, ForbiddenException {
        String email = jwtHelper.getUsernameFromToken(dto.getToken());
        Users user = userRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));

        List<Users> members = dto.getUsersId().stream()
                .map(UUID::fromString)
                .map(id -> {
                    try {
                        return userRepo.findById(id).orElseThrow(() -> new BadRequestException("User not found"));
                    } catch (BadRequestException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        for (Users member : members) {
            if (!friendshipService.areFriends(member.getId(), user.getId())) {
                throw new ForbiddenException("You can only add friends to the room");
            }
        }

        members.add(user); // Add the current user to the list of members

        ChatRooms chatRooms = ChatRooms.builder()
                .roomName(dto.getRoomName())
                .createdBy(user)
                .users(members)
                .build();

        chatRoomRepo.save(chatRooms);
        return user.getId().toString();
    }

    public void addUserToRoom(UUID roomId, List<String> usersId, String token) throws ForbiddenException, BadRequestException {
        String email = jwtHelper.getUsernameFromToken(token);
        Users user = userRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
        ChatRooms chatRooms = chatRoomRepo.findById(roomId).orElseThrow(() -> new BadRequestException("Room not found"));
        for (String userId : usersId) {
            if (!friendshipService.areFriends(UUID.fromString(userId), user.getId())) {
                throw new ForbiddenException("You can only add friends to the room");
            }
        }
        List<Users> members = userRepo.findAllById(usersId.stream().map(UUID::fromString).toList());
        members.add(user);
        chatRooms.getUsers().addAll(members);
        chatRoomRepo.save(chatRooms);

    }
}
