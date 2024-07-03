package com.labhesh.Todos.Todos.app.team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.Name;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTeamDto {

    @NotBlank(message = "Team Name is required")
    @Size(min = 3, max = 50, message = "Team Name should be between 3 and 50 characters")
    private String name;
    @NotBlank(message = "Team Lead is required")
    private String teamLead;
    private List<String> teamMembers;



}
