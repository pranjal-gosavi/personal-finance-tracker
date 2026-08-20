package com.finance.personal_finance_tracker.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.finance.personal_finance_tracker.model.MonthlyBudget;

public interface MonthlyBudgetRepository extends JpaRepository<MonthlyBudget, Long> {
    Optional<MonthlyBudget> findByYearAndMonth(int year, int month);
    boolean existsByYearAndMonth(int year, int month);
}
