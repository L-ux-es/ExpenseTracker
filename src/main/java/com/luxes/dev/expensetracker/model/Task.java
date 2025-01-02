package com.luxes.dev.expensetracker.model;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;



public class Task {


    private long id;
    @NotBlank(message = "Description should not be blank.")
    private String description;
    @NotBlank(message = "Category should not be blank.")
    private String category;
    private LocalDate dateCreation;

    private double cost;

    public Task() {
    }

    public Task(long id, String description, String category, LocalDate dateCreation, double cost) {
        this.id = id;
        setDescription(description);
        setCategory(category);
        setDateCreation(dateCreation);
        setCost(cost);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", date='" + dateCreation + '\'' +
                ", cost=" + cost +
                '}';
    }
}

