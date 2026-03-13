package com.expenses.repositories;

import com.expenses.entities.TransactionEntity;
import com.expenses.entities.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {

    Page<TransactionEntity> findByUserIdAndType(int userId, TransactionType type, Pageable pageable);

    Page<TransactionEntity> findByUserIdAndTypeAndCategoryIdIn(int userId, TransactionType type, List<Integer> categoriesId, Pageable pageable);

}