package com.finance.personal_finance_tracker.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.finance.personal_finance_tracker.dto.CategorySummary;
import com.finance.personal_finance_tracker.exception.ResourceNotFoundException;
import com.finance.personal_finance_tracker.model.Expense;
import com.finance.personal_finance_tracker.repository.ExpenseRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseRepository expenseRepository;

    public ExpenseController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @GetMapping
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    @PostMapping
    public Expense createExpense(@Valid  @RequestBody Expense expense) {
        if (expense.getDate() == null) {
            expense.setDate(LocalDate.now()); // auto-fill date
        }
        return expenseRepository.save(expense);
    }
    
    @GetMapping("/summary/category")
    public List<CategorySummary> getCategorySummary() {
        List<Object[]> results = expenseRepository.getCategoryWiseTotals();
        if (results.isEmpty()) {
            throw new ResourceNotFoundException("No expenses found in any category.");
        }
        return results.stream()
                .map(r -> new CategorySummary((String) r[0], (Double) r[1]))
                .toList();
    }

    
    @GetMapping("/summary/category/by-date")
    public List<CategorySummary> getCategorySummaryByDate(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "category", required = false) String category) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }

        List<Object[]> results;

        if (category != null && !category.isBlank()) {
            boolean categoryExists = expenseRepository.existsByCategoryIgnoreCase(category);
            if (!categoryExists) {
                throw new ResourceNotFoundException("Category '" + category + "' does not exist in expenses.");
            }
            results = expenseRepository.getCategoryWiseTotalsByDateAndCategory(startDate, endDate, category);
        } else {
            results = expenseRepository.getCategoryWiseTotalsByDate(startDate, endDate);
        }

        if (results.isEmpty()) {
            throw new ResourceNotFoundException("No expenses found for the given date range" +
                    (category != null ? " and category '" + category + "'" : "") + ".");
        }

        return results.stream()
                .map(r -> new CategorySummary((String) r[0], (Double) r[1]))
                .toList();
    }
    

    @GetMapping("/summary/category/all")
    public List<CategorySummary> getCategorySummaryAllTime() {
        List<Object[]> results = expenseRepository.getCategoryWiseTotals();
        return results.stream()
                      .map(r -> new CategorySummary((String) r[0], (Double) r[1]))
                      .toList();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @Valid  @RequestBody Expense updatedExpense) {
        Expense existingExpense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense with ID " + id + " not found"));

        existingExpense.setDescription(updatedExpense.getDescription());
        existingExpense.setAmount(updatedExpense.getAmount());
        existingExpense.setDate(updatedExpense.getDate());
        existingExpense.setCategory(updatedExpense.getCategory());

        Expense savedExpense = expenseRepository.save(existingExpense);
        return ResponseEntity.ok(savedExpense);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense with ID " + id + " not found"));
        expenseRepository.delete(expense);
        return ResponseEntity.noContent().build();
    }


}
