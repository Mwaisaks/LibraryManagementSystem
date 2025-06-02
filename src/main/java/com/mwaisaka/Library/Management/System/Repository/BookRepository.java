package com.mwaisaka.Library.Management.System.Repository;

import com.mwaisaka.Library.Management.System.Dto.request.BookRequest;
import com.mwaisaka.Library.Management.System.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Integer> {

    List<BookRequest> searchBooks(String keyword);
}

