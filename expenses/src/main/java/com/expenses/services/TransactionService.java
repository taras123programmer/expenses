package com.expenses.services;

import com.expenses.DTOs.TransactionDTO;
import com.expenses.DTOs.TransactionPageDTO;
import com.expenses.DTOs.TransactionRequestDTO;
import com.expenses.entities.Budget;
import com.expenses.entities.TransactionEntity;
import com.expenses.entities.TransactionType;
import com.expenses.repositories.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    final private TransactionRepository transactionRepository;
    final private int PAGE_SIZE = 10;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionEntity createTransaction(Budget budget, TransactionRequestDTO transactionDTO){
        TransactionEntity transaction = new TransactionEntity(budget.getUserId(), budget.getId(), transactionDTO.type(),
                transactionDTO.date(), transactionDTO.categoryId(), transactionDTO.amount(), transactionDTO.comment());

        transactionRepository.save(transaction);

        return transaction;
    }

    public TransactionPageDTO getTransactionPage(int userId, TransactionType type, int pageNumber, String sortMethod, List<Integer> categoriesId){
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by(sortMethod).descending() );

        Page<TransactionEntity> transactionsPage;
        if(categoriesId==null) {
            transactionsPage = transactionRepository.findByUserIdAndType(userId, type, pageable);
        }
        else{
            transactionsPage = transactionRepository.findByUserIdAndTypeAndCategoryIdIn(userId, type, categoriesId, pageable);
        }

        List<TransactionDTO> transactionsList = transactionsPage.stream().map(TransactionService::getDTO).toList();

        return new TransactionPageDTO(transactionsList, pageNumber, transactionsPage.isLast());
    }


    public static TransactionDTO getDTO(TransactionEntity transaction){
        return new TransactionDTO(transaction.getId(), transaction.getUserId(), transaction.getBudgetId(), transaction.getType(), transaction.getCategoryId(),
                transaction.getCategory().getName(), transaction.getAmount(), transaction.getComment(), transaction.getDate());
    }

}
