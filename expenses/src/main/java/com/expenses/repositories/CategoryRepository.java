package com.expenses.repositories;

import com.expenses.entities.Category;
import com.expenses.entities.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findAllByType(TransactionType transactionType);

    List<Category> findAllByTypeAndRegular(TransactionType type, boolean regular);

}
