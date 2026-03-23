package com.expenses.services;

import com.expenses.DTOs.TemplateDTO;
import com.expenses.entities.Template;
import com.expenses.entities.TransactionEntity;
import com.expenses.entities.TransactionType;
import com.expenses.repositories.TemplateRepository;
import com.expenses.repositories.TransactionRepository;
import jakarta.transaction.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TemplateService {

    final private TemplateRepository templateRepository;
    final private TransactionRepository transactionRepository;

    public TemplateService(TemplateRepository templateRepository, TransactionRepository transactionRepository) {
        this.templateRepository = templateRepository;
        this.transactionRepository = transactionRepository;
    }

    public void createTemplate(TemplateDTO templateDTO){
        Template template = new Template(templateDTO.categoryId(), templateDTO.userId(), templateDTO.amount(), templateDTO.type(), templateDTO.regular());
        templateRepository.save(template);
    }

    public List<TemplateDTO> getTemplateList(int userId, TransactionType type){
        List<Template> templates = templateRepository.findAllByUserIdAndType(userId, type);
        return templates.stream().map(template -> new TemplateDTO(
                                template.getUserId(),
                                template.getType(),
                                template.getCategoryId(),
                                template.getCategory().getName(),
                                template.getAmount(),
                                template.getRegular()
                )).toList();
    }

    @Transactional
    public TransactionEntity applyTemplate(Template template, int budgetId){
        TransactionEntity transaction = new TransactionEntity(template.getUserId(), budgetId, template.getType(),
                LocalDate.now(),template.getCategoryId(), template.getAmount(), template.getCategory().getName());

        transactionRepository.save(transaction);
        return transaction;
    }



}
