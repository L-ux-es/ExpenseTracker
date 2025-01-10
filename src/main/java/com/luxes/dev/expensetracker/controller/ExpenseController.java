package com.luxes.dev.expensetracker.controller;

import com.luxes.dev.expensetracker.model.CustomDate;
import com.luxes.dev.expensetracker.model.Expense;
import com.luxes.dev.expensetracker.model.User;
import com.luxes.dev.expensetracker.repository.ExpenseRepository;
import com.luxes.dev.expensetracker.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseController(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("User not authenticated.");
        }
        String username = authentication.getName();
        return userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found." + username));
    }

    @GetMapping("")
    ResponseEntity<List<Expense>> findAll() {
        return ResponseEntity.ok(expenseRepository.findAllByUserId(getAuthenticatedUser().id()));
    }

    @GetMapping("/{id}")
    ResponseEntity<Expense> findById(@PathVariable int id) {
        Optional<Expense> task = expenseRepository.findById(id, getAuthenticatedUser().id());
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@Valid @RequestBody Expense expense) {
        expenseRepository.create(expense, getAuthenticatedUser().id());
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("")
    void update(@Valid @RequestBody Expense expense) {
        expenseRepository.update(expense, getAuthenticatedUser().id());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable int id) {
        expenseRepository.delete(id, getAuthenticatedUser().id());
    }


    @GetMapping("category/{category}")
    ResponseEntity<List<Expense>> findByCategory(@Valid @PathVariable String category) {
        return ResponseEntity.ok(expenseRepository.findByCategory(category, getAuthenticatedUser().id()));
    }

    @GetMapping("/pastweek")
    ResponseEntity<List<Expense>> filterByWeek() {
        return ResponseEntity.ok(expenseRepository.filterByWeek(1, getAuthenticatedUser().id()));
    }

    @GetMapping("/lastmonth")
    ResponseEntity<List<Expense>> filterByLastMonth() {
        return ResponseEntity.ok(expenseRepository.filterByMonth(1, getAuthenticatedUser().id()));
    }

    @GetMapping("/3months")
    ResponseEntity<List<Expense>> filterBy3Months() {
        return ResponseEntity.ok(expenseRepository.filterByLastMonths(3, getAuthenticatedUser().id()));
    }

    @GetMapping("/months/{cantMonths}")
    ResponseEntity<List<Expense>> filterByMonths(@PathVariable int cantMonths) {
        return ResponseEntity.ok(expenseRepository.filterByLastMonths(cantMonths, getAuthenticatedUser().id()));
    }

    @GetMapping("/date")
    ResponseEntity<List<Expense>> filterByDate(@RequestBody CustomDate customDate) {
        LocalDate startDate = customDate.startDate();
        LocalDate finishDate = customDate.finishDate();
        return ResponseEntity.ok(expenseRepository.filterByDates(startDate, finishDate, getAuthenticatedUser().id()));
    }

    @GetMapping("/cost/{cost}")
    ResponseEntity<List<Expense>> filterByCost(@PathVariable double cost) {
        return ResponseEntity.ok(expenseRepository.filterByCostMinorOrEqualTo(cost, getAuthenticatedUser().id()));
    }

    @GetMapping("/count")
    ResponseEntity<Integer> count() {
        return ResponseEntity.ok(expenseRepository.count(getAuthenticatedUser().id()));
    }
}
