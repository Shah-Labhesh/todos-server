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

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class MessageService {

    private final UsersRepo userRepo;
    private final JwtTokenUtil jwtHelper;
    private final ChatRoomRepo chatRoomRepo;
    private final MessageRepo messageRepo;
    private final FriendshipService friendshipService;


    public List<ChatRooms> getChatRoom(String token) throws BadRequestException {
        String email = jwtHelper.getUsernameFromToken(token);
        Users users = userRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
        return chatRoomRepo.findAllByUsers(users);
    }

    public void createMessage(UUID roomId, String token, String message) throws BadRequestException, ForbiddenException {
        String email = jwtHelper.getUsernameFromToken(token);
        Users users = userRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
        ChatRooms chatRooms = chatRoomRepo.findById(roomId).orElseThrow(() -> new BadRequestException("Room not found"));
        if (!chatRooms.getUser().contains(users)) {
            throw new ForbiddenException("You are not allowed to send message in this room");
        }
        Message messages = Message.builder()
                .chatRoom(chatRooms)
                .message(message)
                .sender(users)
                .build();
        messageRepo.save(messages);
    }

    public List<Message> getRoomMessages(UUID roomId, String token) throws BadRequestException, ForbiddenException {
        String email = jwtHelper.getUsernameFromToken(token);
        Users users = userRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
        ChatRooms chatRooms = chatRoomRepo.findById(roomId).orElseThrow(() -> new BadRequestException("Room not found"));
        if (!chatRooms.getUser().contains(users)) {
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
        List<Users> members = userRepo.findAllById(dto.getUsersId().stream().map(UUID::fromString).toList());
        for (String userId : dto.getUsersId()) {
            if (!friendshipService.areFriends(UUID.fromString(userId), user.getId())) {
                throw new ForbiddenException("You can only add friends to the room");
            }
        }
        members.add(user);
        ChatRooms chatRooms = ChatRooms.builder()
                .roomName(dto.getRoomName())
                .createdBy(user)
                .user(members)
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
        chatRooms.getUser().addAll(members);
        chatRoomRepo.save(chatRooms);

    }
}
