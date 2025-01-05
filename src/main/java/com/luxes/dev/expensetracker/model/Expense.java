package com.luxes.dev.expensetracker.model;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;


public record Expense(
        int id,
        @NotBlank(message = "Description should not be blank.")
        String description,
        String category,
        LocalDate dateCreation,
        double cost
) {
}

