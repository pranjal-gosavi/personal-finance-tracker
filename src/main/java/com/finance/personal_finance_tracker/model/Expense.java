package com.finance.personal_finance_tracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Description is required")
    private String description;

    @Positive(message = "Amount must be positive")
    private double amount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    
    @NotBlank(message = "Category is required")
    private String category;
    // Constructors
    public Expense() {}

    public Expense(String description, double amount, LocalDate date,String category) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
