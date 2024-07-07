package com.labhesh.Todos.Todos.app.messaging;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CreateChatRoomDto {
    private String roomName;
    private String token;
    private List<String> usersId;


}
