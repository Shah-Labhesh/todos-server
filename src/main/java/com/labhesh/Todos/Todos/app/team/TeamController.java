package com.labhesh.Todos.Todos.app.team;

import com.labhesh.Todos.Todos.exception.BadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Team", description = "Team related endpoints")
@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
@SecurityRequirement(name = "auth")
public class TeamController {

    private final TeamService teamService;

     @Operation(summary = "Create a new team", description = "team lead can create a new team and add members to the team")
     @PostMapping
     public ResponseEntity<?> createTeam(@Valid @RequestBody CreateTeamDto createTeamDto) throws BadRequestException {
         return teamService.createTeam(createTeamDto);
     }

     @Operation(summary = "Add a new member to a team", description = "team lead can add a new member to the team. The member must be a friend of the team lead.")
     @PostMapping("/add-member")
     public ResponseEntity<?> addMember(@Valid @RequestBody AddMemberDto addMemberDto) throws BadRequestException {
         return teamService.addTeamMember(addMemberDto);
     }

     @Operation(summary = "Get all teams of a user", description = "Get all teams of a user")
     @GetMapping("/get-teams")
     public ResponseEntity<?> getTeams() throws BadRequestException {
         return teamService.getTeams();
     }

     @Operation(summary = "Get a team by id", description = "Get a team by id")
     @GetMapping("/get-team/{teamId}")
     public ResponseEntity<?> getTeam(@PathVariable String teamId) {
         return teamService.getTeam(teamId);
     }

//     @GetMapping("/get-team-members/{teamId}")
//     public ResponseEntity<?> getTeamMembers(@PathVariable String teamId) {
//         return teamService.getTeamMembers(teamId);
//     }
//
//     @GetMapping("/get-team-lead/{teamId}")
//     public ResponseEntity<?> getTeamLead(@PathVariable String teamId) {
//         return teamService.getTeamLead(teamId);
//     }


}
