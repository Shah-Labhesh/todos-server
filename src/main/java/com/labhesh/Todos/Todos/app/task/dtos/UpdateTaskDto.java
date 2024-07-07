package com.labhesh.Todos.Todos.app.task.dtos;

import com.labhesh.Todos.Todos.app.task.Priority;
import com.labhesh.Todos.Todos.validation.EnumValidator;
import jakarta.validation.constraints.*;


import com.labhesh.Todos.Todos.validation.ValidTimestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateTaskDto {
    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Title must not contain special characters and must be at least 3 characters long")
    private String title;
    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Description must not contain special characters and must be at least 3 characters long")
    private String description;
    @EnumValidator(enumClass = Priority.class, message = "Priority should be one of LOW, MEDIUM, HIGH")
    private String priority;
    @ValidTimestamp(message = "Due date must be in the future", isFuture = true)
    private String dueDate;
    private List<String> members;
}
