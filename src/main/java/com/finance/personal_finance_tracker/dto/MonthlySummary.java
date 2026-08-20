package com.finance.personal_finance_tracker.dto;

import java.time.LocalDate;

public class MonthlySummary {

    private LocalDate startDate;
    private LocalDate endDate;
    private double totalIncome;
    private double totalExpense;
    private double netSavings;

    public MonthlySummary(LocalDate startDate, LocalDate endDate,
                          double totalIncome, double totalExpense,
                          double netSavings) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.netSavings = netSavings;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public double getNetSavings() {
        return netSavings;
    }

    public void setNetSavings(double netSavings) {
        this.netSavings = netSavings;
    }
}
