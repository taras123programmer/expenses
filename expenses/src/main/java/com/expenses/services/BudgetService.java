package com.expenses.services;

import com.expenses.DTOs.BudgetDTO;
import com.expenses.DTOs.TemplateDTO;
import com.expenses.DTOs.TransactionDTO;
import com.expenses.DTOs.TransactionRequestDTO;
import com.expenses.entities.*;
import com.expenses.repositories.BudgetRepository;
import com.expenses.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.springframework.stereotype.Service;
import org.jfree.chart.ChartFactory;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final TransactionService transactionService;
    private final TemplateService templateService;
    private final UserService userService;
    private final UserRepository userRepository;

    public BudgetService(BudgetRepository budgetRepository, TransactionService transactionService,
                         TemplateService templateService, UserService userService, UserRepository userRepository) {
        this.budgetRepository = budgetRepository;
        this.transactionService = transactionService;
        this.templateService = templateService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Transactional
    public Budget createNewBudget(int userId, BigDecimal plannedLimit) {
        LocalDate today = LocalDate.now();
        Budget previous = budgetRepository.getUnclosed(userId);
        if (previous != null) {
            closeBudget(previous.getId());
        }
        Budget newBudget = new Budget(userId, today.getMonthValue(), today.getYear(), plannedLimit);
        newBudget = budgetRepository.save(newBudget);
        User user = userRepository.getById(userId);
        newBudget.setTransactions(new ArrayList<>());
        for (Template template : user.getTemplates()) {
            if (template.getRegular()) {
                TransactionEntity transaction = templateService.applyTemplate(template, newBudget.getId());
                newBudget.getTransactions().add(transaction);
            }
        }

        budgetRepository.save(newBudget);

        return newBudget;
    }

    @Transactional
    public void closeBudget(int budgetId) {
        Budget budget = budgetRepository.getById(budgetId);
        BigDecimal remaining = BigDecimal.ZERO;
        for (TransactionEntity transaction : budget.getTransactions()) {
            if (transaction.getType() == TransactionType.income) {
                remaining = remaining.add(transaction.getAmount());
            } else {
                remaining = remaining.subtract(transaction.getAmount());
            }
        }
        budget.setRemaining(remaining);
        budget.setClosed(true);
        budgetRepository.save(budget);

        userService.updateBalance(budget.getUserId(), remaining);
    }

    public BudgetDTO getCurrentBudget(int userId) {
        LocalDate today = LocalDate.now();
        Budget budget = budgetRepository.getByUserIdAndYearAndMonth(userId, today.getYear(), today.getMonthValue());

        if (budget == null) {
            budget = createNewBudget(userId, BigDecimal.ZERO);
        }

        BigDecimal incomes = BigDecimal.valueOf(0), expenses = BigDecimal.valueOf(0);
        if (budget.getTransactions() != null) {
            for (TransactionEntity transaction : budget.getTransactions()) {
                if (transaction.getType() == TransactionType.income) {
                    incomes = incomes.add(transaction.getAmount());
                } else {
                    expenses = expenses.add((transaction.getAmount()));
                }
            }
        }

        return new BudgetDTO(budget.getUserId(), budget.getPlannedLimit().floatValue(), incomes.floatValue(), expenses.floatValue(),
                incomes.subtract(expenses).floatValue(), budget.getClosed());

    }


    @Transactional
    public void addTransaction(int userId, TransactionRequestDTO transactionDTO) throws BadRequestException {
        LocalDate date = transactionDTO.date();
        Budget budget = budgetRepository.getByUserIdAndYearAndMonth(userId, date.getYear(), date.getMonthValue());

        if (budget == null || budget.getClosed()) {
            throw new BadRequestException();
        }

        TransactionEntity transaction = transactionService.createTransaction(budget, transactionDTO);
        budget.getTransactions().add(transaction);
        budgetRepository.save(budget);

        if (transactionDTO.regular()) {
            templateService.createTemplate(new TemplateDTO(transaction.getUserId(), transaction.getType(),
                    transaction.getCategoryId(), null, transaction.getAmount(), true));
        }

    }


    public List<TransactionDTO> getIncomes(int userId) {
        LocalDate today = LocalDate.now();
        Budget budget = budgetRepository.getByUserIdAndYearAndMonth(userId, today.getYear(), today.getMonthValue());

        List<TransactionDTO> incomes = new ArrayList<>();
        for (TransactionEntity transaction : budget.getTransactions()) {
            if (transaction.getType() == TransactionType.income) {
                incomes.add(TransactionService.getDTO(transaction));
            }
        }
        incomes.sort(Comparator.comparing(TransactionDTO::getDate).reversed());

        return incomes;
    }

    public byte[] getExpensePieChart(int userId) {
        LocalDate today = LocalDate.now();
        Budget budget = budgetRepository.getByUserIdAndYearAndMonth(userId, today.getYear(), today.getMonthValue());

        Map<String, BigDecimal> expenses = new HashMap<>();
        if (budget.getTransactions() != null) {
            for (TransactionEntity transaction : budget.getTransactions()) {
                if (transaction.getType() == TransactionType.expense) {
                    String category = transaction.getCategory().getName();
                    if (!expenses.containsKey(category)) {
                        expenses.put(category, transaction.getAmount());
                    } else {
                        expenses.put(category, expenses.get(category).add(transaction.getAmount()));
                    }
                }
            }
        }
        DefaultPieDataset dataset = new DefaultPieDataset();

        expenses.forEach(dataset::setValue);

        JFreeChart chart = ChartFactory.createPieChart(
                "",
                dataset,
                true,
                false,
                false
        );

        PiePlot plot = (PiePlot) chart.getPlot();

        plot.setLabelLinksVisible(false);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{2}",
                new DecimalFormat("0.00"),
                new DecimalFormat("0%")
        ));

        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0}: {1}",
                new DecimalFormat("0.00"),
                new DecimalFormat("0%")
        ));

        plot.setBackgroundPaint(null);
        plot.setOutlineVisible(false);
        plot.setShadowPaint(null);
        plot.setSectionOutlinesVisible(false);
        chart.getLegend().setItemFont(new Font("Arial", Font.PLAIN, 14));

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(baos, chart, 850, 650);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


}