package com.labhesh.Todos.Todos.app.messaging;

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

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by", referencedColumnName = "uuid")
    private Users createdBy;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "chat_user",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "uuid")
    )
    private List<Users> user;

    @Builder.Default
    private Timestamp createdDate = new Timestamp(System.currentTimeMillis());
    private Timestamp updatedDate;
    private Timestamp deletedDate;
}