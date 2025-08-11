package com.mwaisaka.Library.Management.System.service;

import com.mwaisaka.Library.Management.System.domain.dto.BookDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public interface BookService {
    BookDTO addBook(BookDTO bookDTO);

    BookDTO updateBook(UUID id, BookDTO bookDTO);

    boolean deleteBook(UUID id);

    BookDTO getBookById(UUID id);

    List<BookDTO> getAllBooks();

    List<BookDTO> searchBooks(String keyword);

   // boolean existsByTitleAndAuthor(String title, String author);
}
