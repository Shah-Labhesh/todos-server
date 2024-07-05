package com.labhesh.Todos.Todos.app.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepo extends JpaRepository<Users, UUID>{
    Optional<Users> findByEmail(String email);
    Optional<Users> findByVerificationToken(String token);
    Optional<Users> findByResetPasswordToken(String token);

    List<Users> findAllByEmailIn(List<String> teamMembers);

    @Query("SELECT u from Users u where u.avatarPath = :avatar")
    Optional<Users> findByAvatarPath(@Param("avatar") String avatar);
}
