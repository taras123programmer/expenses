package com.expenses.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "budget")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Byte month;

    @Column(nullable = false)
    private Short year;

    @Column(name="planned_limit", nullable = false, precision = 8, scale = 2)
    private BigDecimal plannedLimit;

    @Column(nullable = true, precision = 8, scale = 2)
    private BigDecimal remaining;

    @Column(nullable = false)
    private Boolean closed;

    @Column(name = "user_id")
    private int userId;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private User user;

    @OneToMany(mappedBy = "budget")
    private List<TransactionEntity> transactions;

    public Budget(){}

    public Budget(int userId, int month, int year, BigDecimal plannedLimit){
        this.userId = userId;
        this.month = (byte) month;
        this.year = (short) year;
        this.plannedLimit = plannedLimit;
        this.closed = false;
    }

    // ===== GETTERS & SETTERS =====

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getMonth() {
        return month;
    }

    public void setMonth(Byte month) {
        this.month = month;
    }

    public Short getYear() {
        return year;
    }

    public void setYear(Short year) {
        this.year = year;
    }

    public BigDecimal getPlannedLimit() {
        return plannedLimit;
    }

    public void setPlannedLimit(BigDecimal plannedLimit) {
    }

    public BigDecimal getRemaining() {
        return remaining;
    }

    public void setRemaining(BigDecimal remaining) {
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<TransactionEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionEntity> transactions) {
        this.transactions = transactions;
    }

}