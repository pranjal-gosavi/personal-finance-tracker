package com.finance.personal_finance_tracker.controller;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.finance.personal_finance_tracker.dto.MonthlySummary;
import com.finance.personal_finance_tracker.repository.ExpenseRepository;
import com.finance.personal_finance_tracker.repository.IncomeRepository;

@RestController
@RequestMapping("/api/summary")
@CrossOrigin(origins = "http://localhost:4200")
public class SummaryController {

    private final ExpenseRepository expenseRepo;
    private final IncomeRepository incomeRepo;

    public SummaryController(ExpenseRepository expenseRepo, IncomeRepository incomeRepo) {
        this.expenseRepo = expenseRepo;
        this.incomeRepo = incomeRepo;
    }

    @GetMapping("/monthly")
    public MonthlySummary getMonthlySummary(
            @RequestParam int year,
            @RequestParam int month) {

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        Double incomeValue = incomeRepo.sumByDateBetween(start, end);
        Double expenseValue = expenseRepo.sumByDateBetween(start, end);

        double income = incomeValue != null ? incomeValue : 0;
        double expense = expenseValue != null ? expenseValue : 0;

        double savings = income - expense;
        System.out.println(" "+income+""+expense+""+""+savings);
        return new MonthlySummary(start, end, income, expense, savings);
    }
}
