package com.luxes.dev.expensetracker.model;

import java.time.LocalDate;

public record CustomDate(
        LocalDate startDate,
        LocalDate finishDate
) {
}