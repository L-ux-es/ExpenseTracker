package com.luxes.dev.expensetracker.repository;

import com.luxes.dev.expensetracker.model.Category;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository {
    private final JdbcClient jdbcClient;

    public CategoryRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }


    public List<Category> findAll() {
        return jdbcClient.sql("SELECT * from category").query(Category.class).list();
    }

    public Optional<Category> findById(int id) {
        return jdbcClient.sql("SELECT id,category.category FROM category WHERE id =?")
                .param(id).query(Category.class).optional();
    }

    public Optional<Category> findByCategory(String category) {
        return jdbcClient.sql("SELECT id,category.category FROM category WHERE LOWER(category.category) = LOWER(?)")
                .param(category).query(Category.class).optional();
    }

    public int findIdByCategory(String category) {
        Optional<Category> categoryOptional = findByCategory(category);
        return categoryOptional.map(Category::id).orElse(-1);
    }


    public void create(Category category) {
        var created = jdbcClient.sql("INSERT INTO category (category) VALUES (?)")
                .params(category.category()).update();
        Assert.state(created == 1, "Failed to create category" + category.category());
    }

    public void update(Category category, int id) {
        var updated = jdbcClient.sql("UPDATE category SET category=? WHERE id=?")
                .params(category.category(), id)
                .update();
        Assert.state(updated == 1, "Failed to update category" + category.category());
    }

    public void delete(int id) {
        var deleted = jdbcClient.sql("DELETE FROM category WHERE id = ?").param(id).update();
        Assert.state(deleted == 1, "Failed to delete category with id " + id);
    }

    public int count() {
        return jdbcClient.sql("SELECT count(*) FROM category").query(Integer.class).single();
    }

    public void saveAll(List<Category> categories) {
        categories.forEach(this::create);
    }

}
