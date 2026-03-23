package com.expenses.controllers;

import com.expenses.DTOs.*;
import com.expenses.entities.UserDetailsImpl;
import com.expenses.services.BudgetService;
import com.expenses.services.CategoryService;
import com.expenses.services.TransactionService;
import com.expenses.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class MainController {

    final private BudgetService budgetService;
    final private UserService userService;

    public MainController(BudgetService budgetService,
                          UserService userService) {
        this.budgetService = budgetService;
        this.userService = userService;

    }

    @GetMapping("/")
    public String dashboard(@AuthenticationPrincipal UserDetailsImpl user, Model model){

        BudgetDTO budget = budgetService.getCurrentBudget(user.getId());
        model.addAttribute("budget", budget);
        model.addAttribute("balance", userService.getBalance(user.getId()));

        return "dashboard";
    }

    @GetMapping("/chart")
    public void getExpenseChart(HttpServletResponse response,
                                @AuthenticationPrincipal UserDetailsImpl user) throws IOException {

        byte[] image = budgetService.getExpensePieChart(user.getId());

        response.setContentType("image/png");
        response.getOutputStream().write(image);
    }

}