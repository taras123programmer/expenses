package com.expenses.repositories;

import com.expenses.entities.Template;
import com.expenses.entities.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemplateRepository extends JpaRepository<Template, Integer> {

    List<Template> findAllByUserIdAndType(int userId, TransactionType type);

}