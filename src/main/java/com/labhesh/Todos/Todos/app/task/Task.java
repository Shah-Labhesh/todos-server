package com.labhesh.Todos.Todos.app.task;


import com.labhesh.Todos.Todos.app.user.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, name = "uuid", updatable = false)
    private UUID id;

    private String title;
    private String description;
    @Builder.Default
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime dueDate;
    @Builder.Default
    private double progress = 0;
    @Builder.Default
    private boolean isCompleted = false;
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by", referencedColumnName = "uuid")
    private Users createdBy;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "todo_members",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "uuid")
    )
    private List<Users> members;
}
