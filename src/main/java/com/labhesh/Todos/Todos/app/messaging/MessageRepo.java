package com.labhesh.Todos.Todos.app.messaging;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepo extends JpaRepository<Message, UUID> {

    @Query("SELECT m FROM Message m WHERE m.chatRoom = ?1 ORDER BY m.sentAt DESC")
    List<Message> findAllByChatRoom(ChatRooms chatRooms);
}
