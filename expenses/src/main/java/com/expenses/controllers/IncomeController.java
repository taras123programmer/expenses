package com.expenses.controllers;

import com.expenses.DTOs.CategoryDTO;
import com.expenses.DTOs.TransactionDTO;
import com.expenses.DTOs.TransactionRequestDTO;
import com.expenses.entities.TransactionType;
import com.expenses.entities.UserDetailsImpl;
import com.expenses.services.BudgetService;
import com.expenses.services.CategoryService;
import com.expenses.services.TransactionService;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/income/")
public class IncomeController
{

    final private BudgetService budgetService;
    final private CategoryService categoryService;
    final private TransactionService transactionService;

    public IncomeController(BudgetService budgetService, CategoryService categoryService, TransactionService transactionService) {
        this.budgetService = budgetService;
        this.categoryService = categoryService;
        this.transactionService = transactionService;
    }

    @GetMapping("/new/")
    public String addIncome(Model model) throws BadRequestException {

        List<CategoryDTO> categories = categoryService.getCategories(TransactionType.income, null);
        model.addAttribute("categories", categories);

        return "new_income";
    }

    @PostMapping("/new/")
    public String addIncome(
            @RequestParam BigDecimal amount,
            @RequestParam LocalDate date,
            @RequestParam Integer categoryId,
            @RequestParam(defaultValue = "false") boolean regular,
            @RequestParam(required = false) String comment,
            @AuthenticationPrincipal UserDetailsImpl user
    ) throws BadRequestException {

        TransactionRequestDTO transaction = new TransactionRequestDTO(TransactionType.income, categoryId, amount, comment, date, regular);

        try {
            budgetService.addTransaction(user.getId(), transaction);
        }
        catch (BadRequestException e){}

        return "redirect:/";
    }

    @GetMapping("/")
    public String IncomeList(@AuthenticationPrincipal UserDetailsImpl user,
                                Model model){

        List<TransactionDTO> incomes = budgetService.getIncomes(user.getId());
        model.addAttribute("incomes", incomes);

        return "incomes";
    }

}
