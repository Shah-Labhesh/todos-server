package com.labhesh.Todos.Todos.app.task;


import com.labhesh.Todos.Todos.app.task.dtos.AddTaskDto;
import com.labhesh.Todos.Todos.app.task.dtos.UpdateTaskDto;
import com.labhesh.Todos.Todos.exception.BadRequestException;
import com.labhesh.Todos.Todos.exception.ForbiddenException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/task")
@RestController
@Tag(name = "Task", description = "Task API")
@SecurityRequirement(name = "auth")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Create a task")
    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody @Valid AddTaskDto dto) throws BadRequestException {
        return taskService.createTask(dto);
    }

    @GetMapping
    public ResponseEntity<?> getMyTask() throws BadRequestException {
        return taskService.myTask();
    }

    @PostMapping("/progress/{taskId}")
    public ResponseEntity<?> updateProgress(@PathVariable String taskId, @RequestParam(required = true)  Double progress ) throws ForbiddenException, BadRequestException {
        return taskService.updateProgress(taskId,progress);
    }

    @Operation(summary = "Update a task")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable String id, @RequestBody @Valid UpdateTaskDto dto) throws BadRequestException, ForbiddenException {
        return taskService.updateTask(id, dto);
    }

    @Operation(summary = "Delete a task")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable String id) throws BadRequestException, ForbiddenException {
        return taskService.deleteTask(id);
    }
}
