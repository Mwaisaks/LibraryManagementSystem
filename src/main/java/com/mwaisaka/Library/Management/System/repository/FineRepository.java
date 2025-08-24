package com.mwaisaka.Library.Management.System.repository;

import com.mwaisaka.Library.Management.System.domain.enums.FineStatus;
import com.mwaisaka.Library.Management.System.domain.models.Fine;
import com.mwaisaka.Library.Management.System.domain.models.Transaction;
import com.mwaisaka.Library.Management.System.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {

    List<Fine> findByUserOrderByCreatedDateDesc(User user);

    @Query("SELECT f FROM Fine f ORDER BY f.createdDate DESC")
    List<Fine> findAllOrderByCreatedDateDesc();

    List<Fine> findByUserAndStatus(User user, FineStatus status);

    List<Fine> findByUserAndStatusOrderByCreatedDateAsc(User user, FineStatus status);

    Optional<Fine> findByTransaction(Transaction transaction);

    @Query("SELECT SUM(f.amount) FROM Fine f WHERE f.user = :user AND f.status = 'UNPAID'")
    Optional<BigDecimal> getTotalUnpaidFinesByUser(@Param("user") User user);

    @Query("SELECT f FROM Fine f WHERE f.status = 'UNPAID' AND f.dueDate < CURRENT_DATE")
    List<Fine> findOverdueFines();

    @Query("SELECT COUNT(f) FROM Fine f WHERE f.user = :user AND f.status = 'UNPAID'")
    int countUnpaidFinesByUser(@Param("user") User user);

    @Query("SELECT f FROM Fine f WHERE f.user = :user AND f.status IN ('UNPAID', 'PARTIALLY_PAID')")
    List<Fine> findOutstandingFinesByUser(@Param("user") User user);
}
