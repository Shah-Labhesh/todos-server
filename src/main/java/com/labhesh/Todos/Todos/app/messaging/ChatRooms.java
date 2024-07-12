package com.labhesh.Todos.Todos.app.messaging;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.labhesh.Todos.Todos.app.user.Users;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.*;

@Entity
@Table(name = "ChatRooms")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRooms {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, name = "uuid", updatable = false)
    private UUID id;

    private String roomName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by", referencedColumnName = "uuid")
    private Users createdBy;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "chat_room_users",
            joinColumns = @JoinColumn(name = "chat_room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<Users> users;

    @Builder.Default
    private Timestamp createdDate = new Timestamp(System.currentTimeMillis());
    private Timestamp updatedDate;
    private Timestamp deletedDate;

    private String lastMessage;
    private Timestamp lastMessageTime;
    private UUID lastMessageBy;
}