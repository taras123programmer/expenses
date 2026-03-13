package com.expenses.services;

import com.expenses.DTOs.BudgetDTO;
import com.expenses.DTOs.TemplateDTO;
import com.expenses.DTOs.TransactionDTO;
import com.expenses.DTOs.TransactionRequestDTO;
import com.expenses.entities.Budget;
import com.expenses.entities.TransactionEntity;
import com.expenses.entities.TransactionType;
import com.expenses.repositories.BudgetRepository;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyComponentPathImpl;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final TransactionService transactionService;
    private final TemplateService templateService;

    public BudgetService(BudgetRepository budgetRepository, TransactionService transactionService, TemplateService templateService) {
        this.budgetRepository = budgetRepository;
        this.transactionService = transactionService;
        this.templateService = templateService;
    }

    @Transactional
    public Budget createNewBudget(int userId){
        LocalDate today = LocalDate.now();
        Budget newBudget = new Budget(userId, today.getMonthValue(), today.getYear(), 0);
        budgetRepository.save(newBudget);
        return newBudget;
    }

    public BudgetDTO getCurrentBudget(int userId){
        LocalDate today = LocalDate.now();
        Budget budget = budgetRepository.getByUserIdAndYearAndMonth(userId, today.getYear(), today.getMonthValue());

        if(budget==null){
            budget = createNewBudget(userId);
            return new BudgetDTO(userId, 0, 0, 0,0, false);
        }

        BigDecimal incomes = BigDecimal.valueOf(0), expenses = BigDecimal.valueOf(0);
        for(TransactionEntity transaction : budget.getTransactions()){
            if(transaction.getType() == TransactionType.income){
                incomes = incomes.add(transaction.getAmount());
            }
            else{
                expenses = expenses.add((transaction.getAmount()));
            }
        }

        return new BudgetDTO(budget.getUserId(), budget.getPlannedLimit().floatValue(), incomes.floatValue(), expenses.floatValue(),
                incomes.subtract(expenses).floatValue(), budget.getClosed());

    }


    @Transactional
    public void addTransaction(int userId, TransactionRequestDTO transactionDTO) throws BadRequestException {
        LocalDate date = transactionDTO.date();
        Budget budget = budgetRepository.getByUserIdAndYearAndMonth(userId, date.getYear(), date.getMonthValue());

        if(budget == null || budget.getClosed()){
            throw new BadRequestException();
        }

        TransactionEntity transaction = transactionService.createTransaction(budget, transactionDTO);
        budget.getTransactions().add(transaction);
        budgetRepository.save(budget);

        if(transactionDTO.regular()){
            templateService.createTemplate(new TemplateDTO(transaction.getUserId(), transaction.getType(),
                    transaction.getCategoryId(), transaction.getAmount(), true));
        }

    }


    public List<TransactionDTO> getIncomes(int userId) {
        LocalDate today = LocalDate.now();
        Budget budget = budgetRepository.getByUserIdAndYearAndMonth(userId, today.getYear(), today.getMonthValue());

        List<TransactionDTO> incomes = new ArrayList<>();
       for(TransactionEntity transaction : budget.getTransactions()){
           if(transaction.getType() == TransactionType.income){
               incomes.add(TransactionService.getDTO(transaction));
           }
       }
       incomes.sort(Comparator.comparing(TransactionDTO::getDate).reversed());

       return incomes;
    }


}
