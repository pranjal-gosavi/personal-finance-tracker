package com.finance.personal_finance_tracker.controller;

import com.finance.personal_finance_tracker.dto.CategorySummary;
import com.finance.personal_finance_tracker.exception.ResourceNotFoundException;
import com.finance.personal_finance_tracker.model.Income;
import com.finance.personal_finance_tracker.repository.IncomeRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/incomes")
public class IncomeController {

    private final IncomeRepository incomeRepository;

    public IncomeController(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    @GetMapping
    public List<Income> getAllIncomes() {
        return incomeRepository.findAll();
    }

    @PostMapping
    public Income createIncome(@Valid @RequestBody Income income) {
        if (income.getDate() == null) {
            income.setDate(LocalDate.now());
        }
        return incomeRepository.save(income);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Income> updateIncome(@PathVariable Long id, @Valid @RequestBody Income updatedIncome) {
        Income existing = incomeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Income with ID " + id + " not found"));

        existing.setDescription(updatedIncome.getDescription());
        existing.setAmount(updatedIncome.getAmount());
        existing.setDate(updatedIncome.getDate());
        existing.setCategory(updatedIncome.getCategory());

        Income saved = incomeRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {
        Income existing = incomeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Income with ID " + id + " not found"));
        incomeRepository.delete(existing);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary/category")
    public List<CategorySummary> getCategorySummaryAllTime() {
        List<Object[]> results = incomeRepository.getCategoryWiseTotals();
        if (results.isEmpty()) {
            throw new ResourceNotFoundException("No incomes found in any category.");
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
            boolean exists = incomeRepository.existsByCategoryIgnoreCase(category);
            if (!exists) {
                throw new ResourceNotFoundException("Category '" + category + "' does not exist in incomes.");
            }
            results = incomeRepository.getCategoryWiseTotalsByDateAndCategory(startDate, endDate, category);
        } else {
            results = incomeRepository.getCategoryWiseTotalsByDate(startDate, endDate);
        }

        if (results.isEmpty()) {
            throw new ResourceNotFoundException("No incomes found for the given date range" +
                    (category != null ? " and category '" + category + "'" : "") + ".");
        }

        return results.stream()
                .map(r -> new CategorySummary((String) r[0], (Double) r[1]))
                .toList();
    }
}
