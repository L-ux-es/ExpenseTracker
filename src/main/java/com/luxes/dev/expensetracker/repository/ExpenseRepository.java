package com.luxes.dev.expensetracker.repository;

import com.luxes.dev.expensetracker.model.Expense;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ExpenseRepository {

    private final JdbcClient jdbcClient;

    public ExpenseRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Expense> findAll() {
        return jdbcClient.sql("SELECT * from expense").query(Expense.class).list();
    }

    public Optional<Expense> findById(int id) {
        return jdbcClient.sql("SELECT id,description,category,date_creation,cost FROM expense WHERE id =?")
                .param(id).query(Expense.class).optional();
    }


    public void create(Expense expense) {
        var created = jdbcClient.sql("INSERT INTO expense VALUES (?,?,?,?,?)")
                .params(List.of(expense.id(), expense.description(), expense.category(), expense.dateCreation(), expense.cost())).update();
        Assert.state(created == 1, "Failed to create expense" + expense.description());
    }

    public void update(Expense expense, int id) {
        var updated = jdbcClient.sql("UPDATE expense SET description=?, category=?,date_creation=?,cost=? WHERE id=?")
                .params(List.of(expense.description(), expense.category(), expense.dateCreation(), expense.cost(), id))
                .update();
        Assert.state(updated == 1, "Failed to update expense" + expense.description());
    }

    public void delete(int id) {
        var deleted = jdbcClient.sql("DELETE FROM expense WHERE id = ?").param(id).update();
        Assert.state(deleted == 1, "Failed to delete expense with id " + id);
    }

    public int count() {
        return jdbcClient.sql("SELECT count(*) FROM expense").query().listOfRows().size();
    }

    public void saveAll(List<Expense> expenses) {
        expenses.forEach(this::create);
    }

    public List<Expense> findByCategory(String category) {
        return jdbcClient.sql("SELECT * FROM expense WHERE LOWER(expense.category) = LOWER(?)")
                .param(category).query(Expense.class).list();
    }

    public List<Expense> filterByWeek(int week) {
        LocalDate dateToSearch = LocalDate.now().minusWeeks(week);
        LocalDate mondayOfDateToSearch = dateToSearch.with(DayOfWeek.MONDAY);
        LocalDate sundayOfDateToSearch = dateToSearch.with(DayOfWeek.SUNDAY);
        return filterByDates(mondayOfDateToSearch, sundayOfDateToSearch);
    }

    public List<Expense> filterByMonth(int month) {
        LocalDate dateToSearch = LocalDate.now().minusMonths(month);
        int yearToSearch = dateToSearch.getYear();
        int monthValue = dateToSearch.getMonthValue();
        int finalDay = dateToSearch.lengthOfMonth();
        LocalDate initialDate = LocalDate.of(yearToSearch, monthValue, 1);
        LocalDate finalDate = LocalDate.of(yearToSearch, monthValue, finalDay);
        return filterByDates(initialDate, finalDate);
    }

    public List<Expense> filterByLastMonths(int month) {
        LocalDate dateToSearch = LocalDate.now().minusMonths(month);
        int yearToSearch = dateToSearch.getYear();
        int monthValue = dateToSearch.getMonthValue();
        LocalDate initialDate = LocalDate.of(yearToSearch, monthValue, 1);
        LocalDate finalDate = LocalDate.now();
        return filterByDates(initialDate, finalDate);
    }

    public List<Expense> filterByDates(LocalDate startDate, LocalDate finishDate) {
        return jdbcClient.sql("SELECT * FROM expense WHERE date_creation BETWEEN ? AND ?")
                .params(List.of(startDate, finishDate)).query(Expense.class).list();
    }

    public List<Expense> filterByCostMinorOrEqualTo(double cost) {
        return jdbcClient.sql("SELECT * FROM expense WHERE expense.cost <= ?").param(cost).query(Expense.class).list();
    }
}
