package com.finance.personal_finance_tracker.repository;

import com.finance.personal_finance_tracker.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    boolean existsByCategoryIgnoreCase(String category);

    @Query("SELECT i.category, SUM(i.amount) FROM Income i GROUP BY i.category")
    List<Object[]> getCategoryWiseTotals();
    
    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM Income i WHERE i.date BETWEEN :start AND :end")
    Double sumByDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);


    @Query("SELECT i.category, SUM(i.amount) FROM Income i " +
           "WHERE i.date BETWEEN :startDate AND :endDate GROUP BY i.category")
    List<Object[]> getCategoryWiseTotalsByDate(@Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);

    @Query("SELECT i.category, SUM(i.amount) FROM Income i " +
           "WHERE LOWER(i.category) = LOWER(:category) " +
           "AND i.date BETWEEN :startDate AND :endDate GROUP BY i.category")
    List<Object[]> getCategoryWiseTotalsByDateAndCategory(@Param("startDate") LocalDate startDate,
                                                          @Param("endDate") LocalDate endDate,
                                                          @Param("category") String category);
}
