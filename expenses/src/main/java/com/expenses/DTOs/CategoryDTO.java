package com.expenses.DTOs;

import com.expenses.entities.TransactionType;

public record CategoryDTO(int id, TransactionType type, String name, boolean custom) {}
