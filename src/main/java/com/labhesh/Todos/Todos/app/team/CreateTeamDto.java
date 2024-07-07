package com.labhesh.Todos.Todos.app.team;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTeamDto {

    @NotBlank(message = "Team Name is required")
    @Size(min = 3, max = 50, message = "Team Name should be between 3 and 50 characters")
    private String name;

    @NotBlank(
            message = "team members cannot be empty"
    )
    private List<String> teamMembers;



}
