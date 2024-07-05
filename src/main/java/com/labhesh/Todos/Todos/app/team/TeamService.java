package com.labhesh.Todos.Todos.app.team;

import com.labhesh.Todos.Todos.app.friendship.FriendshipService;
import com.labhesh.Todos.Todos.app.user.Users;
import com.labhesh.Todos.Todos.app.user.UsersRepo;
import com.labhesh.Todos.Todos.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

    private final TeamsRepo teamsRepository;
    private final UsersRepo usersRepository;
    private final FriendshipService friendshipService;


    public ResponseEntity<?> createTeam(CreateTeamDto createTeamDto) throws BadRequestException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Users teamLead = usersRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));


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
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Users> newMembers = new ArrayList<>();
        Users teamLead = usersRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
        for (String newMemberId : addMemberDto.getTeamMembers()) {
            if (!friendshipService.areFriends(teamLead.getId(), UUID.fromString(newMemberId))) {
                throw new BadRequestException("Users must be friends to join the same team");
            }
            newMembers.add(usersRepository.findById(UUID.fromString(newMemberId)).orElseThrow(()->new BadRequestException("New member not found")));
        }

        List<Users> teamMembers = team.getTeamMembers();
        teamMembers.addAll(newMembers);
        team.setTeamMembers(teamMembers);
        if (addMemberDto.getTeamName() != null){
            team.setName(addMemberDto.getTeamName());
        }
        team.setTeamUpdatedDate(new Timestamp(System.currentTimeMillis()));
        teamsRepository.save(team);
        return ResponseEntity.ok(team);
    }


    public ResponseEntity<?> getTeam(String teamId) {
        return ResponseEntity.ok(teamsRepository.findById(UUID.fromString(teamId)).orElse(null));
    }

    public ResponseEntity<?> getTeams() throws BadRequestException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
        return ResponseEntity.ok(teamsRepository.findAllByTeamLeadIdOrTeamMembersId(user.getId())
        );
    }
}