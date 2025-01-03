package com.luxes.dev.expensetracker.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;


public record Expense(
        int id,
        @NotBlank(message = "Description should not be blank.")
        String description,
        @NotBlank(message = "Category should not be blank.")
        String category,
        LocalDate dateCreation,
        double cost
) {
}

