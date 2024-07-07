package com.labhesh.Todos.Todos.app.task;

import com.labhesh.Todos.Todos.app.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface TaskRepo extends JpaRepository<Task, UUID> {

    @Query("SELECT t FROM Task t JOIN t.members m WHERE m = :user")
    List<Task> findAllByMembers(@Param("user") Users user);

}
