package com.luxes.dev.expensetracker.repository;

import com.luxes.dev.expensetracker.model.Task;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {

    private final JdbcClient jdbcClient;

    public TaskRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Task> findAll() {
        return jdbcClient.sql("SELECT * from expense").query(Task.class).list();
    }

    public Optional<Task> findById(int id) {
        return jdbcClient.sql("SELECT id,description,category,date_creation,cost FROM expense WHERE id =?").param(id).query(Task.class).optional();
    }


    public void create(Task task) {
        var created = jdbcClient.sql("INSERT INTO expense VALUES (?,?,?,?,?)")
                .params(List.of(task.id(), task.description(), task.category(), task.dateCreation(), task.cost())).update();
        Assert.state(created == 1, "Failed to create task" + task.description());
    }

    public void update(Task task, int id) {
        var updated = jdbcClient.sql("UPDATE expense SET description=?, category=?,date_creation=?,cost=? WHERE id=?")
                .params(List.of(task.description(), task.category(), task.dateCreation(), task.cost(), id))
                .update();
        Assert.state(updated == 1, "Failed to update task" + task.description());
    }

    public void delete(int id) {
        var deleted = jdbcClient.sql("DELETE FROM expense WHERE id = ?").param(id).update();
        Assert.state(deleted == 1, "Failed to delete expense with id " + id);
    }

      public int count() {
        return jdbcClient.sql("SELECT count(*) FROM expense").query().listOfRows().size();
    }

    public void saveAll(List<Task> tasks) {
        tasks.stream().forEach(this::create);
    }



}
