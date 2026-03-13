package com.expenses.DTOs;

import com.expenses.entities.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionDTO {

    private int id;
    private int userId;
    private int budgetId;
    private TransactionType type;
    private int categoryId;
    private String category;
    private BigDecimal amount;
    private String comment;
    private LocalDate date;

    public TransactionDTO() {
    }

    public TransactionDTO(int id, int userId, int budgetId, TransactionType type,
                          int categoryId, String category, BigDecimal amount, String comment,
                          LocalDate date) {
        this.id = id;
        this.userId = userId;
        this.budgetId = budgetId;
        this.type = type;
        this.categoryId = categoryId;
        this.category = category;
        this.amount = amount;
        this.comment = comment;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public int getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }


    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}