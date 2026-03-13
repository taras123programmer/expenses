package com.expenses.DTOs;

import com.expenses.entities.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequestDTO(
        TransactionType type,
        int categoryId,
        BigDecimal amount,
        String comment,
        LocalDate date,
        boolean regular
) {}
