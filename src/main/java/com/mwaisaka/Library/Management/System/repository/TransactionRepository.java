package com.mwaisaka.Library.Management.System.repository;

import com.mwaisaka.Library.Management.System.domain.enums.TransactionType;
import com.mwaisaka.Library.Management.System.domain.models.Book;
import com.mwaisaka.Library.Management.System.domain.models.Transaction;
import com.mwaisaka.Library.Management.System.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface TransactionRepository  extends JpaRepository<Transaction, UUID> {

    @Query("SELECT t FROM Transaction t WHERE t.user = :user AND t.book = :book AND t.transactionType = :transactionType AND t.returnDate IS NULL")
    Optional<Transaction> findActiveBorrowTransaction(@Param("user") User user,
                                                      @Param("book") Book book,
                                                      @Param("transactionType") TransactionType transactionType);

}

