package com.expenses.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transaction")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal amount;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(length = 255)
    private String comment;

    @Column(name="budget_id", nullable = false)
    private int budgetId;

    @Column(name="category", nullable = false)
    private int categoryId;

    @Column(name="user_id", nullable = false)
    private int userId;

    @ManyToOne
    @JoinColumn(name = "budget_id", updatable = false, insertable = false)
    private Budget budget;

    @ManyToOne
    @JoinColumn(name = "category", updatable = false, insertable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private User user;

    public TransactionEntity(){}

    public TransactionEntity(int userId, int budgetId, TransactionType type, LocalDate date, int categoryId, BigDecimal amount, String comment ){
        this.userId = userId;
        this.budgetId = budgetId;
        this.type = type;
        this.date = date;
        this.categoryId = categoryId;
        this.amount = amount;
        this.comment = comment;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TransactionType getType() {
        return type;
    }
    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getBudgetId() {
        return budgetId;
    }
    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }

    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer id) {
        this.userId = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Integer id) {
        this.categoryId = id;
    }

}
