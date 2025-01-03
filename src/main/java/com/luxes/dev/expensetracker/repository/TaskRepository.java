package com.luxes.dev.expensetracker.repository;

import com.luxes.dev.expensetracker.model.Task;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {

    private final List<Task> taskList = new ArrayList<>();

    public List<Task> findAll() {
        return taskList;
    }

    public Optional<Task> findById(int id) {
        return taskList.stream().filter(task -> task.id() == id).findFirst();
    }

    public void create(Task task) {
        taskList.add(task);
    }

    public void update(Task task, int id) {
        Optional<Task> taskOptional = findById(id);
        if (taskOptional.isPresent()) {
            taskList.set(taskList.indexOf(taskOptional.get()), task);
        }
    }

    public void delete(int id) {
        taskList.removeIf(task -> task.id()==id);
    }

    @PostConstruct
    private void init() {
        taskList.add(new Task(1, "Buy a candy", "Groceries", LocalDate.now().minusDays(18), 3.09));
        taskList.add(new Task(2, "Buy a chocolate", "Groceries", LocalDate.now().minusWeeks(5), 1.58));
        taskList.add(new Task(3, "Buy a pant", "Clothing", LocalDate.now().minusMonths(1), 15.00));
    }


}
