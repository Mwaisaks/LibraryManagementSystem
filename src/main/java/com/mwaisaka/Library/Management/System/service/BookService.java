package com.mwaisaka.Library.Management.System.service;

import com.mwaisaka.Library.Management.System.domain.dto.BookDTO;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface BookService {
    BookDTO addBook(BookDTO bookDTO);

    BookDTO updateBook(int id, BookDTO bookDTO);

    boolean deleteBook(int id);

    BookDTO getBookById(int id);

    List<BookDTO> getAllBooks();

    List<BookDTO> searchBooks(String keyword);

   // boolean existsByTitleAndAuthor(String title, String author);
}
