package com.mwaisaka.Library.Management.System.service;

import com.mwaisaka.Library.Management.System.domain.dto.BookDTO;

import java.util.List;

public interface BookService {
    BookDTO addBook(BookDTO bookDTO);

    BookDTO updateBook(int id, BookDTO bookDTO);

    boolean deleteBook(int id);

    BookDTO getBookById(int id);

    List<BookDTO> getAllBooks();

    List<BookDTO> searchBooks(String keyword);

   // boolean existsByTitleAndAuthor(String title, String author);
}
