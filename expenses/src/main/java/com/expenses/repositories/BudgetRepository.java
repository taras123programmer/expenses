package com.expenses.repositories;

import com.expenses.entities.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {

    Budget getByUserIdAndYearAndMonth(int userId, int year, int month);

}