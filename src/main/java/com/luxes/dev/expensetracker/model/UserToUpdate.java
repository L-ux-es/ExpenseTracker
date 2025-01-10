package com.luxes.dev.expensetracker.model;

import jakarta.validation.constraints.NotNull;

public record UserToUpdate(
        @NotNull(message = "Password should not be empty")
        String password
) {
}
