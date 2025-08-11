


package com.mwaisaka.Library.Management.System.service.impl;

import com.mwaisaka.Library.Management.System.service.BookService;

import com.mwaisaka.Library.Management.System.domain.dto.BookDTO;
import com.mwaisaka.Library.Management.System.repository.BookRepository;
import com.mwaisaka.Library.Management.System.mapper.BookMapper;
import com.mwaisaka.Library.Management.System.domain.models.Book;
import com.mwaisaka.Library.Management.System.exceptions.BookNotFoundException;
import com.mwaisaka.Library.Management.System.exceptions.ResourceAlreadyExists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    @Transactional
    public BookDTO addBook(BookDTO bookDTO){
        if (bookRepository.existsByTitleAndAuthor(bookDTO.getTitle(), bookDTO.getAuthor())){
            throw new ResourceAlreadyExists("Book with title " + bookDTO.getTitle() + " and author " + bookDTO.getAuthor() + " already exists.");
        }
        Book book = bookMapper.toEntity(bookDTO);
        Book savedBook = bookRepository.save(book);

        return bookMapper.toDto(savedBook);
    }

    public List<BookDTO> getAllBooks(){
        /// Todo : implement pagination
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDTO updateBook(UUID id, BookDTO bookDTO){
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found") );
        bookMapper.updateBookFromDto(bookDTO, book);
        Book updatedBook = bookRepository.save(book);
        return bookMapper.toDto(updatedBook);
    }

    @Override
    public boolean deleteBook(UUID id){
        Optional<Book> bookOptional = bookRepository.findById(id);
        if(bookOptional.isPresent()){
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public BookDTO getBookById(UUID bookId) {
        if(bookId.equals(new UUID(0, 0))){
            throw new IllegalArgumentException("Invalid book id");
        }

        Book book = bookRepository.findById(bookId).orElseThrow(() ->
                new BookNotFoundException("Book not found with id: " + bookId));
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDTO> searchBooks(String keyword) {
        List<Book> books = bookRepository.searchBooks(keyword);
        return books.stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
