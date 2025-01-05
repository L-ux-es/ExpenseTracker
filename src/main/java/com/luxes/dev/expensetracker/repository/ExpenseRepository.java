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
    private final CategoryRepository categoryRepository;

    public ExpenseRepository(JdbcClient jdbcClient, CategoryRepository categoryRepository) {
        this.jdbcClient = jdbcClient;
        this.categoryRepository = categoryRepository;
    }

    public List<Expense> findAll() {
        return jdbcClient.sql("SELECT expense.id,description,category,date_creation,cost FROM expense,category WHERE category.id=expense.category_fk").query(Expense.class).list();
    }

    public Optional<Expense> findById(int id) {
        return jdbcClient.sql("SELECT expense.id,description,category,date_creation,cost FROM expense,category WHERE expense.id =? AND category.id=expense.category_fk")
                .param(id).query(Expense.class).optional();
    }


    public void create(Expense expense) {
        var created = jdbcClient.sql("INSERT INTO expense (description,category_fk,date_creation,cost) VALUES (?,?,?,?)")
                .params(List.of(expense.description(), getCategoryId(expense), expense.dateCreation(), expense.cost())).update();
        Assert.state(created == 1, "Failed to create expense" + expense.description());
    }

    /**
     * @param expense Expense class
     * @return Return the ID of the category in the database. If it does not exist, return 1.
     **/
    private int getCategoryId(Expense expense) {
        int categoryId = categoryRepository.findIdByCategory(expense.category());
        if (categoryId < 0) {
            categoryId = 1;
        }
        return categoryId;
    }

    public void update(Expense expense, int id) {
        var updated = jdbcClient.sql("UPDATE expense SET description=?, category_fk=?,date_creation=?,cost=? WHERE id=?")
                .params(List.of(expense.description(), getCategoryId(expense), expense.dateCreation(), expense.cost(), id))
                .update();
        Assert.state(updated == 1, "Failed to update expense" + expense.description());
    }

    public void delete(int id) {
        var deleted = jdbcClient.sql("DELETE FROM expense WHERE expense.id = ?").param(id).update();
        Assert.state(deleted == 1, "Failed to delete expense with id " + id);
    }

    public int count() {
        return jdbcClient.sql("SELECT count(*) FROM expense").query(Integer.class).single();
    }

    public void saveAll(List<Expense> expenses) {
        expenses.forEach(this::create);
    }

    public List<Expense> findByCategory(String category) {
        return jdbcClient.sql("SELECT expense.id,description,category,date_creation,cost FROM expense,category WHERE LOWER(category.category) = LOWER(?) AND category.id=expense.category_fk")
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
        return jdbcClient.sql("SELECT expense.id,description,category,date_creation,cost FROM expense,category WHERE (date_creation BETWEEN ? AND ?) AND expense.category_fk= category.id")
                .params(List.of(startDate, finishDate)).query(Expense.class).list();
    }

    public List<Expense> filterByCostMinorOrEqualTo(double cost) {
        return jdbcClient.sql("SELECT expense.id,description,category,date_creation,cost FROM expense,category WHERE expense.cost <= ? AND category.id=expense.category_fk").param(cost).query(Expense.class).list();
    }
}
