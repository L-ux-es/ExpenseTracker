package com.luxes.dev.expensetracker.repository;

import com.luxes.dev.expensetracker.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@Import(CategoryRepository.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    void findAll() {
        List<Category> categories = categoryRepository.findAll();
        assertEquals(7, categories.size());
    }

    @Test
    void findById() {
        Optional<Category> category = categoryRepository.findById(1);
        assertEquals("Groceries", category.get().category());
    }

    @Test
    void findByCategory() {
        Optional<Category> category = categoryRepository.findByCategory("Groceries");
        assertEquals("Groceries", category.get().category());
    }

    @Test
    void create() {
        Category categoryNew = new Category(8, "Category");
        categoryRepository.create(categoryNew);
        assertEquals("Category", categoryRepository.findByCategory("Category").get().category());
    }

    @Test
    void update() {
        Category categoryNew = new Category(7, "New Category");
        System.out.println(categoryRepository.findAll());
        categoryRepository.update(categoryNew, 7);
        assertEquals("New Category", categoryRepository.findByCategory("New Category").get().category());
    }

    @Test
    void delete() {
        categoryRepository.delete(4);
        assertEquals(categoryRepository.findById(4), Optional.empty());
    }

    @Test
    void count() {
        int count = categoryRepository.count();
        assertEquals(categoryRepository.findAll().size(), count);
    }

    @Test
    void saveAll() {
        int count = categoryRepository.count();
        categoryRepository.saveAll(List.of(new Category(8, "New Category"),
                new Category(9, "Other Category")));
        assertEquals(count + 2, categoryRepository.findAll().size());
    }
}