package com.labhesh.Todos.Todos.app.messaging;

import com.labhesh.Todos.Todos.exception.BadRequestException;
import com.labhesh.Todos.Todos.exception.ForbiddenException;
import com.labhesh.Todos.Todos.exception.InternalServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MessagingSocket {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    /**
     * Creates a chat room.
     */
    @MessageMapping("/create-room")
    public void createChatRoom(@Payload Map<String, Object> data) throws BadRequestException, ForbiddenException, InternalServerException {
        Object user = data.get("usersId");
        String token = data.get("token").toString();
        String roomName = data.get("roomName").toString();
        if (user == null || token == null || roomName == null) {
            throw new BadRequestException("Missing required parameters");
        }
        List<String> usersId = new ArrayList<>();
        if (user instanceof List<?>) {
            for (Object id : (List<?>) user) {
                usersId.add(id.toString());
            }
        } else {
            usersId.add(user.toString());
        }
        CreateChatRoomDto dto = new CreateChatRoomDto();
        dto.setRoomName(roomName);
        dto.setToken(token);
        dto.setUsersId(usersId);


        if (dto.getRoomName() == null || dto.getToken() == null || dto.getUsersId() == null) {
            throw new BadRequestException("Missing required parameters");
        }

        String userId = messageService.createChatRoom(dto);
        messagingTemplate.convertAndSend("/topic/" + userId, messageService.getChatRoom(dto.getToken()));
    }

    /**
     * Retrieves the chat rooms for a user.
     */
    @MessageMapping("/get-rooms")
    public void getChatRoom(@Payload Map<String, Object> payload) throws BadRequestException, ForbiddenException, InternalServerException {
        String token = (String) payload.get("token");
        String userId = (String) payload.get("userId");
        if (token == null || userId == null) {
            throw new BadRequestException("Missing required parameters");
        }
        ResponseEntity<?> response = messageService.getChatRoom(token);
        messagingTemplate.convertAndSend("/topic/" + userId, response);
    }

    /**
     * Processes a message and sends it to the specified room.
     */
    @MessageMapping("/message")
    public void processMessage(@Payload Map<String, Object> payload) throws BadRequestException, InternalServerException, ForbiddenException {
        UUID roomId = UUID.fromString((String) payload.get("roomId"));
        String token = (String) payload.get("token");
        String message = (String) payload.get("message");
        if (roomId.toString().isEmpty() || token == null || message == null) {
            throw new BadRequestException("Missing required parameters");
        }
        messageService.createMessage(roomId, token, message);
        List<Message> messages = messageService.getRoomMessages(roomId, token);
        messagingTemplate.convertAndSend("/topic/" + roomId, ResponseEntity.ok(messages));
    }

    /**
     * Adds a user to a room if they are friends.
     */
    @MessageMapping("/add-user")
    public void addUserToRoom(@Payload Map<String, Object> payload) throws BadRequestException, ForbiddenException, InternalServerException {
        if (payload.get("roomId") == null || payload.get("token") == null || payload.get("userId") == null) {
            throw new BadRequestException("Missing required parameters");
        }

        UUID roomId;
        String token;
        List<String> usersId;

        try {
            roomId = UUID.fromString((String) payload.get("roomId"));
            token = (String) payload.get("token");

            Object usersIdObject = payload.get("userId");
            if (!(usersIdObject instanceof List<?>)) {
                throw new BadRequestException("Invalid userId format");
            }

            usersId = new ArrayList<>();
            for (Object userIdObj : (List<?>) usersIdObject) {
                if (!(userIdObj instanceof String)) {
                    throw new BadRequestException("Invalid userId format");
                }
                usersId.add((String) userIdObj);
            }

        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }



        messageService.addUserToRoom(roomId, usersId, token);
        List<Message> messages = messageService.getRoomMessages(roomId, token);
        messagingTemplate.convertAndSend("/topic/" + roomId, messages);
    }

    /**
     * Retrieves messages for a specific room.
     */
    @MessageMapping("/get-messages")
    public void getMessages(@Payload Map<String, String> payload) throws BadRequestException, ForbiddenException {
        UUID roomId = UUID.fromString(payload.get("roomId"));
        String token = payload.get("token");
        if (roomId.toString().isEmpty() || token == null) {
            throw new BadRequestException("Missing required parameters");
        }
        List<Message> messages = messageService.getRoomMessages(roomId, token);
        messagingTemplate.convertAndSend("/topic/" + roomId, ResponseEntity.ok(messages));
    }

    /**
     * Deletes a message and updates the room messages.
     */
    @MessageMapping("/delete-message")
    @SendTo("/topic/delete-message")
    public void deleteMessage(@Payload Map<String, Object> payload) throws BadRequestException, ForbiddenException, InternalServerException {
        UUID messageId = UUID.fromString((String) payload.get("messageId"));
        String token = (String) payload.get("token");
        UUID roomId = UUID.fromString((String) payload.get("roomId"));
        if (messageId.toString().isEmpty() || token == null || roomId.toString().isEmpty()) {
            throw new BadRequestException("Missing required parameters");
        }
        messageService.deleteMessage(messageId, token);
        List<Message> messages = messageService.getRoomMessages(roomId, token);
        messagingTemplate.convertAndSend("/topic/" + roomId, messages);
    }
}
