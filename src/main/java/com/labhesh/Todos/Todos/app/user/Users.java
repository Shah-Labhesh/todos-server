package com.labhesh.Todos.Todos.app.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.labhesh.Todos.Todos.app.files.Files;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, name = "uuid", updatable = false)
    private UUID id;
    private String name;
    @JsonIgnore
    private String password;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Files file;
    @Column(unique = true, nullable = false)
    private String email;

    @Builder.Default
    private boolean isVerified = false;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    private String verificationToken;
    private String resetPasswordToken;
    @Builder.Default
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime updatedDate;
    private LocalDateTime deletedDate;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>() {{
            add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return role.name();
                }
            });
        }};
    }

    @Override
    public String getUsername(){
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
