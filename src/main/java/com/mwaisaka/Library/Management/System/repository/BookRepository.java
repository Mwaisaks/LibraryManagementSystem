package com.mwaisaka.Library.Management.System.repository;

import com.mwaisaka.Library.Management.System.domain.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Integer> {

    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.isbn) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Book> searchBooks(@Param("keyword") String keyword);

    boolean existsByTitleAndAuthor(String title, String author);
}

