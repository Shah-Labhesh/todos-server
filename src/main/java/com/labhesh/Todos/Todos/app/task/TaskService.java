package com.labhesh.Todos.Todos.app.task;


import com.labhesh.Todos.Todos.app.friendship.FriendshipService;
import com.labhesh.Todos.Todos.app.task.dtos.AddTaskDto;
import com.labhesh.Todos.Todos.app.task.dtos.UpdateTaskDto;
import com.labhesh.Todos.Todos.app.team.TeamsRepo;
import com.labhesh.Todos.Todos.app.user.Users;
import com.labhesh.Todos.Todos.app.user.UsersRepo;
import com.labhesh.Todos.Todos.exception.BadRequestException;
import com.labhesh.Todos.Todos.exception.ForbiddenException;
import com.labhesh.Todos.Todos.utils.DateUtils;
import com.labhesh.Todos.Todos.utils.SuccessResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepo taskRepo;
    private final TeamsRepo teamsRepo;
    private final UsersRepo usersRepo;
    private final FriendshipService friendshipService;

    private Priority getPriority(String priority) {
        return switch (priority) {
            case "MEDIUM" -> Priority.MEDIUM;
            case "HIGH" -> Priority.HIGH;
            default -> Priority.LOW;
        };
    }

    public ResponseEntity<?> createTask(AddTaskDto dto) throws BadRequestException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users author = usersRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));

        if (dto.getMembers().isEmpty()){
            throw  new BadRequestException("You must assign the task to at least one Team");
        }
        List<Users> members = new ArrayList<>();

        for (String memberId : dto.getMembers()) {
            if (!friendshipService.areFriends(author.getId(), UUID.fromString(memberId))) {
                throw new BadRequestException("You can only assign tasks to your friends");
            }
            members.add(usersRepo.findById(UUID.fromString(memberId)).orElseThrow(() -> new BadRequestException("User not found")));
        }

        members.add(author);

        Task task = Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .dueDate(DateUtils.convertStringToTimestamp(dto.getDueDate()))
                .priority(getPriority(dto.getPriority()))
                .createdBy(author)
                .members(members)
                .build();

        return ResponseEntity.ok(new SuccessResponse("Task created successfully", taskRepo.save(task), null));
    }

    public ResponseEntity<?> updateTask(String id, UpdateTaskDto dto) throws BadRequestException, ForbiddenException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users author = usersRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
    Task task = taskRepo.findById(UUID.fromString(id)).orElseThrow(() -> new BadRequestException("Task not found"));
        if (!task.getCreatedBy().getId().equals(author.getId())) {
            throw new ForbiddenException("You are not authorized to update this task");
        }
        List<Users> members = new ArrayList<>();
        for (String memberId : dto.getMembers()) {
            if (!friendshipService.areFriends(author.getId(), UUID.fromString(memberId))) {
                throw new BadRequestException("You can only assign tasks to your friends");
            }
            members.add(usersRepo.findById(UUID.fromString(memberId)).orElseThrow(() -> new BadRequestException("User not found")));
        }

        List<Users> oldMembers = task.getMembers();

        if (dto.getTitle() != null) {
            task.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            task.setDescription(dto.getDescription());
        }
        if (dto.getDueDate() != null) {
            task.setDueDate(DateUtils.convertStringToTimestamp(dto.getDueDate()));
        }
        if (dto.getPriority() != null) {
            task.setPriority(getPriority(dto.getPriority()));
        }
        oldMembers.addAll(members);
        task.setMembers(oldMembers);
        taskRepo.save(task);
        return ResponseEntity.ok(new SuccessResponse("Task updated successfully", task, null));

    }

    public ResponseEntity<?> deleteTask(String id) throws BadRequestException, ForbiddenException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users author = usersRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
        Task task = taskRepo.findById(UUID.fromString(id)).orElseThrow(() -> new BadRequestException("Task not found"));
        if (!task.getCreatedBy().getId().equals(author.getId())) {
            throw new ForbiddenException("You are not authorized to update this task");
        }
        taskRepo.delete(task);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> myTask() throws BadRequestException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users author = usersRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
        return ResponseEntity.ok().body(taskRepo.findAllByMembers(author));
    }

    public ResponseEntity<?> updateProgress(String taskId,Double progress) throws BadRequestException, ForbiddenException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users author = usersRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
        Task task = taskRepo.findById(UUID.fromString(taskId)).orElseThrow(() -> new BadRequestException("Task not found"));
        if (!task.getCreatedBy().getId().equals(author.getId())) {
            throw new ForbiddenException("You are not authorized to update this task");
        }
        if (task.getProgress() > progress){
            throw new BadRequestException("Progress cannot be decreased");
        }

        task.setProgress(progress);
        taskRepo.save(task);
        return ResponseEntity.ok(new SuccessResponse("Progress updated", task, null));

    }
//
//
//    public ResponseEntity<?> getAllTasks() {
//        return ResponseEntity.ok(taskRepo.findAll());
//    }
//
//    public ResponseEntity<?> getAllTasksByTeam(String teamId) {
//        return ResponseEntity.ok(taskRepo.findAllByTeam(teamsRepo.findById(teamId).get()));
//    }
//
//    public ResponseEntity<?> getAllTasksByUser(String userId) {
//        return ResponseEntity.ok(taskRepo.findAllByMembers(usersRepo.findById(userId).get()));
//    }
}
