package com.luxes.dev.expensetracker.controller;

import com.luxes.dev.expensetracker.exception.ExpenseNotFoundException;
import com.luxes.dev.expensetracker.model.Expense;
import com.luxes.dev.expensetracker.repository.ExpenseRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseRepository expenseRepository;

    public ExpenseController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @GetMapping("")
    List<Expense> findAll() {
        return expenseRepository.findAll();
    }

    @GetMapping("/{id}")
    Expense findById(@PathVariable int id) {
        Optional<Expense> task = expenseRepository.findById(id);
        if (task.isEmpty()) {
            throw new ExpenseNotFoundException();
        }
        return task.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@Valid @RequestBody Expense expense) {
        expenseRepository.create(expense);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@Valid @RequestBody Expense expense, @PathVariable int id) {
        expenseRepository.update(expense, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable int id) {
        expenseRepository.delete(id);
    }
}
