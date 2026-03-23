package com.expenses.controllers;

import com.expenses.DTOs.*;
import com.expenses.entities.Template;
import com.expenses.entities.TransactionType;
import com.expenses.entities.UserDetailsImpl;
import com.expenses.services.BudgetService;
import com.expenses.services.CategoryService;
import com.expenses.services.TemplateService;
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
@RequestMapping("/expense")
public class ExpenseController {

    final private BudgetService budgetService;
    final private CategoryService categoryService;
    final private TransactionService transactionService;
    final private TemplateService templateService;

    public ExpenseController(BudgetService budgetService, CategoryService categoryService,
                             TransactionService transactionService, TemplateService templateService) {
        this.budgetService = budgetService;
        this.categoryService = categoryService;
        this.transactionService = transactionService;
        this.templateService = templateService;
    }

    @GetMapping("/new/")
    public String newExpense(Model model, @RequestParam(defaultValue = "false") boolean regular){

        List<CategoryDTO> categories = categoryService.getCategories(TransactionType.expense, null);
        model.addAttribute("categories", categories);
        model.addAttribute("regular", regular);

        return "new_expense";
    }

    @PostMapping("/new/")
    public String addExpense(
            @RequestParam BigDecimal amount,
            @RequestParam LocalDate date,
            @RequestParam Integer categoryId,
            @RequestParam(required = false) String comment,
            @RequestParam(required = false, defaultValue = "false") boolean regular,
            @AuthenticationPrincipal UserDetailsImpl user
    ) throws BadRequestException {

        TransactionRequestDTO transactionDTO = new TransactionRequestDTO(TransactionType.expense, categoryId,
                amount, comment, date, regular);

        try {
            budgetService.addTransaction(user.getId(), transactionDTO);
        }
        catch (BadRequestException e){}

        return "redirect:/";
    }


    @GetMapping("/")
    public String getExpenseHistory(@RequestParam(defaultValue="0") int page,
                                    @RequestParam(required = false) List<Integer> categories,
                                    @RequestParam(defaultValue = "date") String sort,
                                    @AuthenticationPrincipal UserDetailsImpl user,
                                    Model model){

        List<CategoryDTO> categoryList = categoryService.getCategories(TransactionType.expense, false);
        model.addAttribute("categories", categoryList);

        TransactionPageDTO expensePage = transactionService.getTransactionPage(user.getId(), TransactionType.expense, page, sort, categories);

        model.addAttribute("expenses", expensePage.transactions());
        model.addAttribute("page", page);
        model.addAttribute("is_last", expensePage.isLast());
        model.addAttribute("sort", sort);
        if(categories != null) {
            model.addAttribute("checked_categories", categories);
        }

        return "expenses";
    }

    @GetMapping("/regular/")
    public String RegularExpenseList(@AuthenticationPrincipal UserDetailsImpl user,
                             Model model){

        List<TemplateDTO> expenses = templateService.getTemplateList(user.getId(), TransactionType.expense);
        model.addAttribute("expenses", expenses);

        return "regular_expenses";
    }


}
