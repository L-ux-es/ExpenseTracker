package com.luxes.dev.expensetracker.repository;

import com.luxes.dev.expensetracker.model.Expense;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@Import({ExpenseRepository.class, CategoryRepository.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ExpenseRepositoryTest {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Test
    void findAll() {
        List<Expense> expenses = expenseRepository.findAll();
        assertEquals(expenses.size(), expenseRepository.count());
    }

    @Test
    void findById() {
        Expense expense = expenseRepository.findById(1).get();
        assertEquals("Shop chinese food", expense.description());
    }

    @Test
    void create() {
        Expense newExpense = new Expense(1, "New expense", "Groceries", LocalDate.now(), 10.0);
        expenseRepository.create(newExpense);
        String description = "";
        for (Expense expense : expenseRepository.findAll()) {
            if (expense.description().equals(newExpense.description())) {
                description = expense.description();
            }
        }
        assertEquals(newExpense.description(), description);
    }

    @Test
    void update() {
        Expense newExpense = new Expense(1, "Update expense", "Groceries", LocalDate.now(), 10.0);
        expenseRepository.update(newExpense, 1);
        assertEquals(newExpense.description(), expenseRepository.findById(1).get().description());
    }

    @Test
    void delete() {
        expenseRepository.delete(1);
        assertEquals(Optional.empty(), expenseRepository.findById(1));
    }

    @Test
    void count() {
        int count = expenseRepository.count();
        assertEquals(expenseRepository.findAll().size(), count);
    }

    @Test
    void saveAll() {
        int count = expenseRepository.count();
        expenseRepository.saveAll(List.of(new Expense(1, "New expense", "Groceries", LocalDate.now(), 10.0),
                new Expense(2, "Other expense", "Utilities", LocalDate.now().minusDays(20), 200.0)));
        assertEquals(expenseRepository.findAll().size(), count + 2);
    }

    @Test
    void filterByDates() {
        LocalDate yesterday = LocalDate.now().minusMonths(7);
        expenseRepository.create(new Expense(1, "New expense", "Groceries", yesterday.plusMonths(1), 10.0));
        expenseRepository.create(new Expense(1, "New expense", "Groceries", yesterday, 10.0));
        LocalDate today = LocalDate.now();
        List<Expense> expenseList = expenseRepository.filterByDates(yesterday, today);
        Assert.isTrue(expenseList.size() >= 2, "Almost two expenses");
    }
/*
    @Test
    void findByCategory() {

    }

    @Test
    void filterByWeek() {
    }

    @Test
    void filterByMonth() {
    }

    @Test
    void filterByLastMonths() {
    }



    @Test
    void filterByCostMinorOrEqualTo() {
    }*/
}