package com.luxes.dev.expensetracker.repository;

import com.luxes.dev.expensetracker.model.User;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final JdbcClient jdbcClient;
    private final ExpenseRepository expenseRepository;

    public UserRepository(JdbcClient jdbcClient, ExpenseRepository expenseRepository) {
        this.jdbcClient = jdbcClient;
        this.expenseRepository = expenseRepository;
    }

    public List<User> findAll() {
        return jdbcClient.sql("SELECT * FROM users").query(User.class).list();
    }

    public Optional<User> findById(int id) {
        return jdbcClient.sql("SELECT * FROM users WHERE users.id =?")
                .param(id).query(User.class).optional();
    }


    public void create(User user) {
        var created = jdbcClient.sql("INSERT INTO users (name,password,rol) VALUES (?,?,?)")
                .params(List.of(user.name(), user.password(), user.rol())).update();
        Assert.state(created == 1, "Failed to create user" + user.name());
    }


    public void update(User user, int id) {
        var updated = jdbcClient.sql("UPDATE users SET password=? WHERE id=?")
                .params(List.of(user.password()), id).update();
        Assert.state(updated == 1, "Failed to update expense" + user.name());
    }

    public void delete(int id) {
        if (countByRol("admin") > 1) {
            expenseRepository.deleteByUserId(id);
            var deleted = jdbcClient.sql("DELETE FROM users WHERE users.id = ?").param(id).update();
            Assert.state(deleted == 1, "Failed to delete user with id " + id);
        }
    }

    public int count() {
        return jdbcClient.sql("SELECT count(*) FROM users").query(Integer.class).single();
    }

    public int countByRol(String rol) {
        return jdbcClient.sql("SELECT count(*) FROM users WHERE LOWER(rol) = LOWER(?)").param(rol).query(Integer.class).single();
    }

    public void saveAll(List<User> users) {
        users.forEach(this::create);
    }

    public Optional<User> findByName(String name) {
        return jdbcClient.sql("SELECT * FROM users WHERE users.name = ?")
                .param(name).query(User.class).optional();
    }

    public Optional<User> findUser(String name,String password) {
        return jdbcClient.sql("SELECT * FROM users WHERE name=? AND password=?")
                .params(List.of(name, password)).query(User.class).optional();
    }

}
