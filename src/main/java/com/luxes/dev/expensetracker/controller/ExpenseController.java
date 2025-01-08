package com.luxes.dev.expensetracker.controller;

import com.luxes.dev.expensetracker.exception.ExpenseNotFoundException;
import com.luxes.dev.expensetracker.model.CustomDate;
import com.luxes.dev.expensetracker.model.Expense;
import com.luxes.dev.expensetracker.repository.ExpenseRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private final ExpenseRepository expenseRepository;
    private static final int USER_ID = 1;

    public ExpenseController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @GetMapping("")
    List<Expense> findAll() {
        return expenseRepository.findAllByUserId(USER_ID);
    }

    @GetMapping("/{id}")
    Expense findById(@PathVariable int id) {
        Optional<Expense> task = expenseRepository.findById(id, USER_ID);
        if (task.isEmpty()) {
            throw new ExpenseNotFoundException();
        }
        return task.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@Valid @RequestBody Expense expense) {
        expenseRepository.create(expense, USER_ID);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("")
    void update(@Valid @RequestBody Expense expense) {
        expenseRepository.update(expense, USER_ID);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable int id) {
        expenseRepository.delete(id, USER_ID);
    }


    @GetMapping("category/{category}")
    List<Expense> findByCategory(@Valid @PathVariable String category) {
        return expenseRepository.findByCategory(category, USER_ID);
    }

    @GetMapping("/pastweek")
    List<Expense> filterByWeek() {
        return expenseRepository.filterByWeek(1, USER_ID);
    }

    @GetMapping("/lastmonth")
    List<Expense> filterByLastMonth() {
        return expenseRepository.filterByMonth(1, USER_ID);
    }

    @GetMapping("/3months")
    List<Expense> filterBy3Months() {
        return expenseRepository.filterByLastMonths(3, USER_ID);
    }

    @GetMapping("/months/{cantMonths}")
    List<Expense> filterByMonths(@PathVariable int cantMonths) {
        return expenseRepository.filterByLastMonths(cantMonths, USER_ID);
    }

    @GetMapping("/date")
    List<Expense> filterByDate(@RequestBody CustomDate customDate) {
        LocalDate startDate = customDate.startDate();
        LocalDate finishDate = customDate.finishDate();
        return expenseRepository.filterByDates(startDate, finishDate, USER_ID);
    }

    @GetMapping("/cost/{cost}")
    List<Expense> filterByCost(@PathVariable double cost) {
        return expenseRepository.filterByCostMinorOrEqualTo(cost, USER_ID);
    }
}
