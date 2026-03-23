package com.expenses.repositories;

import com.expenses.entities.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {

    Budget getByUserIdAndYearAndMonth(int userId, int year, int month);

    @Query("SELECT b FROM Budget b WHERE b.closed = false AND b.userId=:userId")
    Budget getUnclosed(int userId);

}