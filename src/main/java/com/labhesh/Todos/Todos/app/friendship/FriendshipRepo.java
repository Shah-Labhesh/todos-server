package com.labhesh.Todos.Todos.app.friendship;

import com.labhesh.Todos.Todos.app.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FriendshipRepo extends JpaRepository<FriendShip, UUID> {
    @Query("SELECT f FROM FriendShip f WHERE f.friend.id = ?1 AND f.id = ?2")
    Optional<FriendShip> findByUserIdAndFriendId(UUID userId, UUID friendId);

    @Query("SELECT f FROM FriendShip f WHERE f.user.id = ?1 AND f.friend.id = ?2 AND f.isAccepted = true")
    Optional<FriendShip> findByUserIdAndFriendIdAndAcceptedIsTrue(UUID userId, UUID friendId);

    @Query("SELECT f FROM FriendShip f WHERE f.user.id = ?1 AND f.friend.id = ?2 AND f.isAccepted = true OR f.user.id = ?2 AND f.friend.id = ?1 AND f.isAccepted = true")
    boolean isMyFriend(UUID userId, UUID friendId);

    @Query("SELECT f FROM FriendShip f WHERE f.user = ?1 OR f.friend = ?1 AND f.isAccepted = true")
    List<FriendShip> findAllByUserAndAcceptedIsTrue(Users user);

    @Query("SELECT f FROM FriendShip f WHERE f.friend.id = ?1 AND f.isAccepted = false")
    List<FriendShip> findAllByFriendIdAndAcceptedIsFalse(UUID userId);
}
