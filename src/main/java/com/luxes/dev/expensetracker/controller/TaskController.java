package com.luxes.dev.expensetracker.controller;

import com.luxes.dev.expensetracker.exception.TaskNotFoundException;
import com.luxes.dev.expensetracker.model.Task;
import com.luxes.dev.expensetracker.repository.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("")
    List<Task> findAll() {
        return taskRepository.findAll();
    }

    @GetMapping("/{id}")
    Task findById(@PathVariable long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            throw new TaskNotFoundException();
        }
        return task.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@Valid @RequestBody Task task) {
        taskRepository.create(task);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@Valid @RequestBody Task task, @PathVariable long id) {
        taskRepository.update(task, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable long id) {
        taskRepository.delete(id);
    }
}
