package com.expenses.DTOs;

public record BudgetDTO(int userId, float planned_limit, float total, float spent, float free, boolean closed) {}
