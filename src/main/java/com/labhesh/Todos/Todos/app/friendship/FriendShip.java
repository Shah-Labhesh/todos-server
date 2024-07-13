package com.labhesh.Todos.Todos.app.friendship;

import com.labhesh.Todos.Todos.app.user.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "friendships")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FriendShip {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, name = "uuid", updatable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "uuid")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "friend_id", referencedColumnName = "uuid")
    private Users friend;

    private UUID sentBy;

    @Builder.Default
    private boolean isAccepted = false;

    @Builder.Default
    private LocalDateTime createdDate = LocalDateTime.now();


}
