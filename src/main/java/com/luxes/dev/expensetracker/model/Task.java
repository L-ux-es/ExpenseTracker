package com.luxes.dev.expensetracker.model;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;


public record Task(
        long id,
        @NotBlank(message = "Description should not be blank.")
        String description,
        @NotBlank(message = "Category should not be blank.")
        String category,
        LocalDate dateCreation,
        double cost
) {}

