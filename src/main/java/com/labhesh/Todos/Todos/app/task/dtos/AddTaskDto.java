package com.labhesh.Todos.Todos.app.task.dtos;


import com.labhesh.Todos.Todos.app.task.Priority;
import com.labhesh.Todos.Todos.validation.EnumValidator;

import com.labhesh.Todos.Todos.validation.ValidTimestamp;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddTaskDto {

    @NotBlank(message = "Title is cannot be empty")
    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Title must not contain special characters and must be at least 3 characters long")
    private String title;
    @NotBlank(message = "Description is cannot be empty")
    @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Description must not contain special characters and must be at least 3 characters long")
    private String description;
    @EnumValidator(enumClass = Priority.class, message = "Priority should be one of LOW, MEDIUM, HIGH")
    private String priority;
//    @NotBlank(message = "Start date is cannot be empty")
//    @Pattern(regexp = "^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/[0-9]{4}$", message = "Start date must be in MM/DD/YYYY format")
//    @FutureOrPresent(message = "Start date must be in the future")
//    private String startDate;
    @ValidTimestamp(message = "Due date must be in the future", isFuture = true)
    private String dueDate;
    private List<String> members;
}
