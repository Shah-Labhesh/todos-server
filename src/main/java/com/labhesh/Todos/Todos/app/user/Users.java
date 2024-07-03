package com.labhesh.Todos.Todos.app.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.labhesh.Todos.Todos.app.friendship.FriendShip;
import com.labhesh.Todos.Todos.app.team.Teams;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, name = "uuid", updatable = false)
    private UUID id;

    @JsonIgnore
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Builder.Default
    private boolean isVerified = false;

    private String verificationToken;
    private String resetPasswordToken;
    private Timestamp createdDate;
    private Timestamp updatedDate;
    private Timestamp deletedDate;

    @PrePersist
    public void prePersist() {
        createdDate = new Timestamp(System.currentTimeMillis());
    }
}
