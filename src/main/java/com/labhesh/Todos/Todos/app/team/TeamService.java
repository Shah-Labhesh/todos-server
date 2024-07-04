package com.labhesh.Todos.Todos.app.team;

import com.labhesh.Todos.Todos.app.friendship.FriendshipService;
import com.labhesh.Todos.Todos.app.user.Users;
import com.labhesh.Todos.Todos.app.user.UsersRepo;
import com.labhesh.Todos.Todos.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

    private final TeamsRepo teamsRepository;
    private final UsersRepo usersRepository;
    private final FriendshipService friendshipService;


    public ResponseEntity<?> createTeam(CreateTeamDto createTeamDto) throws BadRequestException {
        Users teamLead = usersRepository.findById(UUID.fromString(createTeamDto.getTeamLead()))
                .orElseThrow(() -> new BadRequestException("Team lead not found"));

        List<Users> teamMembers = new ArrayList<>();
        for (String memberId : createTeamDto.getTeamMembers()) {
            Users member = usersRepository.findById(UUID.fromString(memberId))
                    .orElseThrow(() -> new BadRequestException("User not found"));
            if (!friendshipService.areFriends(teamLead.getId(), member.getId())) {
                throw new BadRequestException("Users must be friends to join the same team");
            }
            teamMembers.add(member);
            teamMembers.add(teamLead);
        }

        Teams team = Teams.builder()
                .name(createTeamDto.getName())
                .teamLead(teamLead)
                .teamMembers(teamMembers)
                .build();

        return ResponseEntity.ok(teamsRepository.save(team));
    }

    public ResponseEntity<?> addTeamMember(AddMemberDto addMemberDto) throws BadRequestException {
        Teams team = teamsRepository.findById(UUID.fromString(addMemberDto.getTeamId())).orElseThrow(() -> new BadRequestException("Team not found"));
        Users member = usersRepository.findById(UUID.fromString(addMemberDto.getUserId())).orElseThrow(() -> new BadRequestException("User not found"));

        if (friendshipService.areFriends(UUID.fromString(addMemberDto.getUserId()), UUID.fromString(addMemberDto.getTeamId()))) {
            throw new BadRequestException("Users must be friends to join the same team");
        }

        List<Users> teamMembers = team.getTeamMembers();
        teamMembers.add(member);
        team.setTeamMembers(teamMembers);
        teamsRepository.save(team);
        return ResponseEntity.ok(team);
    }


    public ResponseEntity<?> getTeam(String teamId) {
        return ResponseEntity.ok(teamsRepository.findById(UUID.fromString(teamId)).orElse(null));
    }

    public ResponseEntity<?> getTeams(String userId) {
        return ResponseEntity.ok(teamsRepository.findAllByTeamLeadIdOrTeamMembersId(UUID.fromString(userId))
        );
    }
}