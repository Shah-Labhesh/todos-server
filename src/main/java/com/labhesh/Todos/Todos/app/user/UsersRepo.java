package com.labhesh.Todos.Todos.app.user;

import org.springframework.data.jpa.repository.JpaRepository;
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

}
