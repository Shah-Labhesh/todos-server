package com.labhesh.Todos.Todos.app.team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TeamsRepo extends JpaRepository<Teams, UUID> {
    @Query("SELECT t FROM Teams t JOIN t.teamMembers tm WHERE t.teamLead.id = :userId OR tm.id = :userId")
    List<Teams> findAllByTeamLeadIdOrTeamMembersId(@Param("userId") UUID userId);
}
