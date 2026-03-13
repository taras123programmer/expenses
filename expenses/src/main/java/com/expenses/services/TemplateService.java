package com.expenses.services;

import com.expenses.DTOs.TemplateDTO;
import com.expenses.entities.Template;
import com.expenses.entities.TransactionType;
import com.expenses.repositories.TemplateRepository;
import jakarta.transaction.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateService {

    final private TemplateRepository templateRepository;

    public TemplateService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
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
                                template.getAmount(),
                                template.getRegular()
                )).toList();
    }



}
