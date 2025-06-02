package com.mwaisaka.Library.Management.System.Service;

import com.mwaisaka.Library.Management.System.Dto.request.BookRequest;
import com.mwaisaka.Library.Management.System.Repository.BookRepository;
import com.mwaisaka.Library.Management.System.models.Book;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public BookRequest createBook(BookRequest bookRequest){
        Book book = new Book();
        BeanUtils.copyProperties(bookRequest,book);
        Book savedBook = bookRepository.save(book);

        BookRequest savedBookRequest = new BookRequest();
        BeanUtils.copyProperties(savedBook, savedBookRequest);
        return savedBookRequest;
    }
    public List<BookRequest> getAllBooks(){
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(book -> {
                    BookRequest bookRequest = new BookRequest();
                    BeanUtils.copyProperties(book, bookRequest);
                    return bookRequest;
                })
                .collect(Collectors.toList());
    }
    public BookRequest updateBook(Integer id, BookRequest bookRequest){
        Optional<Book> bookOptional = bookRepository.findById(id);
        if(bookOptional.isPresent()){
            Book book = bookOptional.get();

            if(bookRequest.getTitle() != null) book.setTitle(bookRequest.getTitle());
            if(bookRequest.getAuthor() != null) book.setAuthor(bookRequest.getAuthor());
            if(bookRequest.getPublisher() != null) book.setPublisher(bookRequest.getPublisher());
            if(bookRequest.getIsbn() != null) book.setIsbn(bookRequest.getIsbn());
            if(bookRequest.getPublishedDate() != null) book.setPublishedDate(bookRequest.getPublishedDate());
            if(bookRequest.getTotalCopies() != null) book.setTotalCopies(bookRequest.getTotalCopies());
            if(bookRequest.getAvailableCopies() != null) book.setAvailableCopies(bookRequest.getAvailableCopies());

            Book updatedBook = bookRepository.save(book);

            BookRequest updatedBookRequest = new BookRequest();
            BeanUtils.copyProperties(updatedBook, updatedBookRequest);
            return updatedBookRequest;
        }
        return null;
    }
    public boolean deleteBook(Integer id){
        Optional<Book> bookOptional = bookRepository.findById(id);
        if(bookOptional.isPresent()){
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public BookRequest getBookById(int id) {
        Book book = bookRepository.findById(id).orElse(null);

        BookRequest bookRequest = new BookRequest();

    }

    public List<BookRequest> searchBooks(String keyword) {
        return bookRepository.searchBooks(keyword);
    }
}
