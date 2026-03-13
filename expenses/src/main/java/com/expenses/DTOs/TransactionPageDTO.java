package com.expenses.DTOs;

import java.util.List;

public record TransactionPageDTO(
    List<TransactionDTO> transactions,
    int page,
    boolean isLast
){}
