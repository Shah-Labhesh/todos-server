package com.labhesh.Todos.Todos.app.team;

import com.labhesh.Todos.Todos.app.user.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "teams")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Teams {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, name = "uuid", updatable = false)
    private UUID id;

    private String name;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "teamLead", referencedColumnName = "uuid")
    private Users teamLead;

    @ManyToMany
    @JoinTable(
            name = "team_members",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "uuid")
    )
    private List<Users> teamMembers;

    private Timestamp teamCreatedDate;
    private Timestamp teamUpdatedDate;
    private Timestamp teamDeletedDate;

    @PrePersist
    public void prePersist() {
        teamCreatedDate = new Timestamp(System.currentTimeMillis());
    }
}
