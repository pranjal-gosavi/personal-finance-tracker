package com.finance.personal_finance_tracker.dto;

public class BudgetStatus {

    private int year;
    private int month;
    private double limitAmount;
    private double spent;
    private double remaining;
    private boolean exceeded;

    public BudgetStatus() {
    }

    public BudgetStatus(int year, int month, double limitAmount,
                        double spent, double remaining, boolean exceeded) {
        this.year = year;
        this.month = month;
        this.limitAmount = limitAmount;
        this.spent = spent;
        this.remaining = remaining;
        this.exceeded = exceeded;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(double limitAmount) {
        this.limitAmount = limitAmount;
    }

    public double getSpent() {
        return spent;
    }

    public void setSpent(double spent) {
        this.spent = spent;
    }

    public double getRemaining() {
        return remaining;
    }

    public void setRemaining(double remaining) {
        this.remaining = remaining;
    }

    public boolean isExceeded() {
        return exceeded;
    }

    public void setExceeded(boolean exceeded) {
        this.exceeded = exceeded;
    }
}