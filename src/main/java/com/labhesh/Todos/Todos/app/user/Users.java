package com.labhesh.Todos.Todos.app.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Users{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, name = "uuid", updatable = false)
    private UUID id;
    @JsonIgnore
    private String password;
    @Column(unique = true)
    private String email;
    @Builder.Default
    private boolean isVerified = false;
    private Timestamp createdDate;
    private Timestamp updatedDate;
    private Timestamp deletedDate;

}
