package com.luxes.dev.expensetracker.repository;

import com.luxes.dev.expensetracker.model.User;
import com.luxes.dev.expensetracker.model.UserToUpdate;
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

    public void create(User user) {
        var created = jdbcClient.sql("INSERT INTO users (name,password,rol) VALUES (?,?,?)")
                .params(List.of(user.name(), user.password(), user.rol().toLowerCase())).update();
        Assert.state(created == 1, "Failed to create user" + user.name());
    }


    public void update(UserToUpdate user, int id) {
        var updated = jdbcClient.sql("UPDATE users SET password=? WHERE id=?")
                .params(List.of(user.password(), id)).update();
        Assert.state(updated == 1, "Failed to update user");
    }

    public void delete(int id) {
        expenseRepository.deleteAll(id);
        jdbcClient.sql("DELETE FROM users WHERE users.id = ?").param(id).update();
    }

    public int count() {
        return jdbcClient.sql("SELECT count(*) FROM users").query(Integer.class).single();
    }

    public Optional<User> findByName(String name) {
        return jdbcClient.sql("SELECT * FROM users WHERE users.name = ?")
                .param(name).query(User.class).optional();
    }

}
