package com.finance.personal_finance_tracker.controller;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.finance.personal_finance_tracker.dto.BudgetStatus;
import com.finance.personal_finance_tracker.model.MonthlyBudget;
import com.finance.personal_finance_tracker.repository.ExpenseRepository;
import com.finance.personal_finance_tracker.repository.MonthlyBudgetRepository;

@RestController
@RequestMapping("/api/budgets")
@CrossOrigin(origins = "http://localhost:4200")
public class BudgetController {

    private final MonthlyBudgetRepository budgetRepo;
    private final ExpenseRepository expenseRepo;

    public BudgetController(MonthlyBudgetRepository budgetRepo, ExpenseRepository expenseRepo) {
        this.budgetRepo = budgetRepo;
        this.expenseRepo = expenseRepo;
    }

    @PostMapping
    public ResponseEntity<MonthlyBudget> upsertBudget(@RequestBody MonthlyBudget budget) {
        return ResponseEntity.ok(
            budgetRepo.findByYearAndMonth(budget.getYear(), budget.getMonth())
                      .map(existing -> {
                          existing.setLimitAmount(budget.getLimitAmount());
                          return budgetRepo.save(existing);
                      })
                      .orElseGet(() -> budgetRepo.save(budget))
        );
    }

    
    @GetMapping("/status")
    public BudgetStatus getStatus(@RequestParam int year, @RequestParam int month) {
        try {
            double limit = budgetRepo.findByYearAndMonth(year, month)
                                     .map(MonthlyBudget::getLimitAmount)
                                     .orElse(0.0);

            LocalDate start = LocalDate.of(year, month, 1);
            LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

            Double spentValue = expenseRepo.sumByDateBetween(start, end);
            double spent = (spentValue != null) ? spentValue : 0.0;
            double remaining = limit - spent;

            System.out.println("in getstatus before return " + remaining + " " + limit + " " + spent);

            return new BudgetStatus(year, month, limit, spent, Math.max(remaining, 0), spent > limit);
        } catch (Exception e) {
            e.printStackTrace();
            throw e; // rethrow to see full stack trace in logs
        }
    }

    
 // ✅ GET all budgets
    @GetMapping
    public ResponseEntity<?> getAllBudgets() {
        return ResponseEntity.ok(budgetRepo.findAll());
    }

    // ✅ GET budget by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getBudgetById(@PathVariable Long id) {
        return budgetRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<MonthlyBudget> updateBudget(@PathVariable Long id, @RequestBody MonthlyBudget updatedBudget) {
        return budgetRepo.findById(id).map(existing -> {
            existing.setYear(updatedBudget.getYear());
            existing.setMonth(updatedBudget.getMonth());
            existing.setLimitAmount(updatedBudget.getLimitAmount());
            return ResponseEntity.ok(budgetRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ✅ DELETE - Remove Budget
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBudget(@PathVariable Long id) {
        return budgetRepo.findById(id).map(existing -> {
            budgetRepo.delete(existing);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

}
