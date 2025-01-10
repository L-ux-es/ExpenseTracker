package com.luxes.dev.expensetracker.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record User(
        int id,
        @NotBlank(message = "Name should not be blank.")
        String name,
        @NotNull(message = "Password should not be empty")
        String password,
        String rol
) {
}
