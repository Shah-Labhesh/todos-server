package com.labhesh.Todos.Todos.app.team;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddMemberDto {

    @NotBlank(message = "User Id is required")
    private String userId;
    @NotBlank(message = "Team Id is required")
    private String teamId;
    @NotBlank(message = "Team Members are required")
    private List<String> teamMembers;
}
