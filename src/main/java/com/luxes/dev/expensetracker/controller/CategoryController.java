package com.luxes.dev.expensetracker.controller;

import com.luxes.dev.expensetracker.exception.CategoryNotFoundException;
import com.luxes.dev.expensetracker.model.Category;
import com.luxes.dev.expensetracker.repository.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("")
    List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    Category findById(@PathVariable int id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new CategoryNotFoundException();
        }
        return category.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@Valid @RequestBody Category category) {
        categoryRepository.create(category);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@Valid @RequestBody Category category, @PathVariable int id) {
        categoryRepository.update(category, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable int id) {
        categoryRepository.delete(id);
    }

    @GetMapping("name/{category}")
    Category findByCategory(@Valid @PathVariable String category) {
        Optional<Category> categoryObject = categoryRepository.findByCategory(category);
        if (categoryObject.isEmpty()) {
            throw new CategoryNotFoundException();
        }
        return categoryObject.get();
    }

}
