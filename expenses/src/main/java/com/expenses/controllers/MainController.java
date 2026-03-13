package com.expenses.controllers;

import com.expenses.DTOs.*;
import com.expenses.entities.UserDetailsImpl;
import com.expenses.services.BudgetService;
import com.expenses.services.CategoryService;
import com.expenses.services.TransactionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    final private BudgetService budgetService;
    final private CategoryService categoryService;
    final private TransactionService transactionService;

    public MainController(BudgetService budgetService, CategoryService categoryService, TransactionService transactionService) {
        this.budgetService = budgetService;
        this.categoryService = categoryService;
        this.transactionService = transactionService;
    }

    @GetMapping("/")
    public String dashboard(@AuthenticationPrincipal UserDetailsImpl user, Model model){

        BudgetDTO budget = budgetService.getCurrentBudget(user.getId());
        model.addAttribute("budget", budget);

        return "dashboard";
    }






}