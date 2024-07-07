package com.labhesh.Todos.Todos.app.team;

import jakarta.validation.constraints.*;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddMemberDto {

    @NotBlank(message = "Team Id is required")
    private String teamId;
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Team Name should contain only alphabets and numbers")
    @Size(min = 3,  message = "Team Name should be at least 3 characters long")
    private String teamName;
    @NotBlank(message = "Team Members are required")
    private List<String> teamMembers;
}
