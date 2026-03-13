package com.expenses.DTOs;

import com.expenses.entities.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TemplateDTO(
        int userId,
        TransactionType type,
        int categoryId,
        BigDecimal amount,
        boolean regular
) {}
