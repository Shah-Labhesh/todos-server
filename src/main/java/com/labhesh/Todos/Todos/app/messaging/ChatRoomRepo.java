package com.labhesh.Todos.Todos.app.messaging;

import com.labhesh.Todos.Todos.app.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatRoomRepo extends JpaRepository<ChatRooms, UUID> {

    @Query("SELECT c FROM ChatRooms c JOIN c.users u WHERE u = :user")
    List<ChatRooms> findAllByUser(Users user);

    // sort by date time of last message
    @Query("SELECT c FROM ChatRooms c JOIN c.users u WHERE u = :user ORDER BY c.lastMessageTime DESC")
    List<ChatRooms> findAllByUserOrderByLastMessageTimeDesc(Users user);
}
