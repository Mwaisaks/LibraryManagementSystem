package com.mwaisaka.Library.Management.System.service.impl;

import com.mwaisaka.Library.Management.System.domain.dto.request.BorrowRequest;
import com.mwaisaka.Library.Management.System.domain.dto.request.ReturnRequest;
import com.mwaisaka.Library.Management.System.domain.enums.TransactionType;
import com.mwaisaka.Library.Management.System.domain.enums.UserRole;
import com.mwaisaka.Library.Management.System.domain.models.Book;
import com.mwaisaka.Library.Management.System.domain.models.Transaction;
import com.mwaisaka.Library.Management.System.models.User;
import com.mwaisaka.Library.Management.System.repository.BookRepository;
import com.mwaisaka.Library.Management.System.repository.TransactionRepository;
import com.mwaisaka.Library.Management.System.repository.UserRepository;
import com.mwaisaka.Library.Management.System.service.BorrowReturnService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

public class BorrowReturnServiceImpl implements BorrowReturnService {

    private  BookRepository bookRepository;

    private  UserRepository userRepository;

    private  TransactionRepository transactionRepository;

    @Override
    public String borrowBook(BorrowRequest borrowRequest) {
        User user = userRepository.findById(borrowRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("user not found"));
        if (user.getRole() == UserRole.LIBRARIAN){
            throw new RuntimeException("Librarians cannot borrow books");
        }

        Book book = bookRepository.findById(borrowRequest.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if(book.getAvailableCopies() <= 0){
            throw new RuntimeException("Book has no available copies");
        }

        Optional<Transaction> existingBorrow = transactionRepository
                .findActiveBorrowTransaction(user,book, TransactionType.BORROW);
        if(existingBorrow.isPresent()){
            throw new RuntimeException("User has already borrowed this book");
        }

        LocalDateTime dueDate = borrowRequest.getDueDate() != null
                ? borrowRequest.getDueDate().atStartOfDay()
                :LocalDateTime.now().plusDays(14);

        book.setAvailableCopies(book.getAvailableCopies() -1);

        if(book.getAvailableCopies() <= 0){
            book.setBorrowedBy(user);
        }

        bookRepository.save(book);

        Transaction transaction= Transaction.builder()
                .user(user)
                .book(book)
                .transactionType(TransactionType.BORROW)
                .transactionDate(LocalDateTime.now())
                .dueDate(dueDate)
                .build();
        transactionRepository.save(transaction);
        return  "Book borrowed successfully.Due date: " + dueDate.toLocalDate();
    }

    @Override
    public String returnBook(ReturnRequest returnRequest) {
         User user = userRepository.findById(returnRequest.getUserId())
                 .orElseThrow(() -> new RuntimeException("User not found"));

         Book book = bookRepository.findById(returnRequest.getBookId())
                 .orElseThrow(() -> new RuntimeException("Book not found"));

         Transaction borrowTransaction = transactionRepository
                 .findActiveBorrowTransaction(user,book,TransactionType.BORROW)
                 .orElseThrow(() -> new RuntimeException("No active borrow record found for this user and book"));

         borrowTransaction.setReturnDate(LocalDateTime.now());
         transactionRepository.save(borrowTransaction);

         Transaction returnTransaction = Transaction.builder()
                 .user(user)
                 .book(book)
                 .transactionType(TransactionType.RETURN)
                 .transactionDate(LocalDateTime.now())
                 .build();
         transactionRepository.save(returnTransaction);

         book.setAvailableCopies(book.getAvailableCopies() + 1);

         if(book.getBorrowedBy() != null && book.getBorrowedBy().equals(user)){
             book.setBorrowedBy(null);
         }
         bookRepository.save(book);
         return "Book returned successfully";
    }
}
