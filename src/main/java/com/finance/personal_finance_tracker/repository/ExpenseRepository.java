package com.finance.personal_finance_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finance.personal_finance_tracker.model.Expense;

import java.time.LocalDate;
import java.util.List;


public interface ExpenseRepository extends JpaRepository<Expense, Long> {

	@Query("SELECT e.category, SUM(e.amount) FROM Expense e GROUP BY e.category")
    List<Object[]> getCategoryWiseTotals();
    
    
    	
    	
    	boolean existsByCategoryIgnoreCase(String category);

        @Query("SELECT e.category, SUM(e.amount) " +
               "FROM Expense e " +
               "WHERE e.date BETWEEN :startDate AND :endDate " +
               "GROUP BY e.category")
        List<Object[]> getCategoryWiseTotalsByDate(@Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);

        @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.date BETWEEN :start AND :end")
        Double sumByDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

        
        @Query("SELECT e.category, SUM(e.amount) " +
               "FROM Expense e " +
               "WHERE LOWER(e.category) = LOWER(:category) " +
               "AND e.date BETWEEN :startDate AND :endDate " +
               "GROUP BY e.category")
        List<Object[]> getCategoryWiseTotalsByDateAndCategory(@Param("startDate") LocalDate startDate,
                                                              @Param("endDate") LocalDate endDate,
                                                              @Param("category") String category);

}