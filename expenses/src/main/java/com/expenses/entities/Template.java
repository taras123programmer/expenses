package com.expenses.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "template")
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private Boolean regular;

    @Column(name="category", nullable = false)
    private int categoryId;

    @Column(name="user_id", nullable = false)
    private int userId;

    @ManyToOne
    @JoinColumn(name = "category", updatable = false, insertable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private User user;

    // ===== Конструктор =====
    public Template() {
    }

    public Template(int categoryId, int userId, BigDecimal amount, TransactionType type, Boolean regular){
        this.amount = amount;
        this.type = type;
        this.regular = regular;
        this.categoryId = categoryId;
        this.userId = userId;
    }

    // ===== Гетери та сетери =====
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

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Boolean getRegular() {
        return regular;
    }

    public void setRegular(Boolean regular) {
        this.regular = regular;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

}