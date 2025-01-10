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

    public List<Expense> findAllByUserId(int userId) {
        return jdbcClient.sql("SELECT expenses.id,description,category,date_creation,cost FROM expenses " +
                        "JOIN categories ON categories.id = expenses.category_fk WHERE expenses.user_fk = ?")
                .param(userId).query(Expense.class).list();
    }

    public Optional<Expense> findById(int id, int userId) {
        return jdbcClient.sql("SELECT expenses.id,description,category,date_creation,cost FROM expenses" +
                        " JOIN categories ON categories.id = expenses.category_fk " +
                        "WHERE expenses.user_fk=? AND expenses.id = ?")
                .params(List.of(userId, id)).query(Expense.class).optional();
    }


    public void create(Expense expense, int userId) {
        var created = jdbcClient.sql("INSERT INTO expenses (description,category_fk,date_creation,cost,user_fk) VALUES (?,?,?,?,?)")
                .params(List.of(expense.description(), getCategoryId(expense), expense.dateCreation(), expense.cost()), userId).update();
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

    public void update(Expense expense, int userId) {
        var updated = jdbcClient.sql("UPDATE expenses SET description=?, category_fk=?,date_creation=?,cost=? WHERE user_fk=? AND id=?")
                .params(List.of(expense.description(), getCategoryId(expense), expense.dateCreation(), expense.cost(), userId, expense.id()))
                .update();
        Assert.state(updated == 1, "Failed to update expense" + expense.description());
    }

    public void delete(int id, int userId) {
        var deleted = jdbcClient.sql("DELETE FROM expenses WHERE user_fk=? AND expenses.id = ?").params(List.of(userId, id)).update();
        Assert.state(deleted == 1, "Failed to delete expense with id " + id);
    }

    public void deleteAll(int userId) {
        var deleted = jdbcClient.sql("DELETE FROM expenses WHERE expenses.user_fk = ?").param(userId).update();
        Assert.state(deleted == 1, "Failed to delete expenses of user with id " + userId);
    }

    public int count(int userId) {
        return jdbcClient.sql("SELECT count(*) FROM expenses WHERE user_fk=?").param(userId).query(Integer.class).single();
    }

    public List<Expense> findByCategory(String category, int userId) {
        return jdbcClient.sql("SELECT expenses.id,description,category,date_creation,cost FROM expenses JOIN categories " +
                        "ON categories.id=expenses.category_fk WHERE user_fk=? AND LOWER(categories.category) = LOWER(?)")
                .params(List.of(userId, category)).query(Expense.class).list();
    }

    public List<Expense> filterByWeek(int week, int userId) {
        LocalDate dateToSearch = LocalDate.now().minusWeeks(week);
        LocalDate mondayOfDateToSearch = dateToSearch.with(DayOfWeek.MONDAY);
        LocalDate sundayOfDateToSearch = dateToSearch.with(DayOfWeek.SUNDAY);
        return filterByDates(mondayOfDateToSearch, sundayOfDateToSearch, userId);
    }

    public List<Expense> filterByMonth(int month, int userId) {
        LocalDate dateToSearch = LocalDate.now().minusMonths(month);
        int yearToSearch = dateToSearch.getYear();
        int monthValue = dateToSearch.getMonthValue();
        int finalDay = dateToSearch.lengthOfMonth();
        LocalDate initialDate = LocalDate.of(yearToSearch, monthValue, 1);
        LocalDate finalDate = LocalDate.of(yearToSearch, monthValue, finalDay);
        return filterByDates(initialDate, finalDate, userId);
    }

    public List<Expense> filterByLastMonths(int month, int userId) {
        LocalDate dateToSearch = LocalDate.now().minusMonths(month);
        int yearToSearch = dateToSearch.getYear();
        int monthValue = dateToSearch.getMonthValue();
        LocalDate initialDate = LocalDate.of(yearToSearch, monthValue, 1);
        LocalDate finalDate = LocalDate.now();
        return filterByDates(initialDate, finalDate, userId);
    }

    public List<Expense> filterByDates(LocalDate startDate, LocalDate finishDate, int userId) {
        return jdbcClient.sql("SELECT expenses.id,description,category,date_creation,cost FROM expenses JOIN categories " +
                        "ON categories.id=expenses.category_fk WHERE expenses.user_fk=? AND date_creation BETWEEN ? AND ?")
                .params(List.of(userId, startDate, finishDate)).query(Expense.class).list();
    }

    public List<Expense> filterByCostMinorOrEqualTo(double cost, int userId) {
        return jdbcClient.sql("SELECT expenses.id,description,category,date_creation,cost FROM expenses JOIN categories " +
                        "ON expenses.category_fk = categories.id  WHERE expenses.user_fk=? AND expenses.cost <= ?")
                .params(List.of(userId, cost)).query(Expense.class).list();
    }
}
