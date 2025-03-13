package com.mwaisaka.Library.Management.System.Service;

import com.mwaisaka.Library.Management.System.Dto.BookDTO;
import com.mwaisaka.Library.Management.System.Repository.BookRepository;
import com.mwaisaka.Library.Management.System.models.Book;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public BookDTO createBook(BookDTO bookDTO){
        Book book = new Book();
        BeanUtils.copyProperties(bookDTO,book);
        Book savedBook = bookRepository.save(book);

        BookDTO savedBookDto = new BookDTO();
        BeanUtils.copyProperties(savedBook,savedBookDto);
        return savedBookDto;
    }
    public List<BookDTO> getAllBooks(){
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(book -> {
                    BookDTO bookDTO = new BookDTO();
                    BeanUtils.copyProperties(book,bookDTO);
                    return bookDTO;
                })
                .collect(Collectors.toList());
    }
    public BookDTO updateBook(Integer id,BookDTO bookDTO){
        Optional<Book> bookOptional = bookRepository.findById(id);
        if(bookOptional.isPresent()){
            Book book = bookOptional.get();

            if(bookDTO.getTitle() != null) book.setTitle(bookDTO.getTitle());
            if(bookDTO.getAuthor() != null) book.setAuthor(bookDTO.getAuthor());
            if(bookDTO.getPublisher() != null) book.setPublisher(bookDTO.getPublisher());
            if(bookDTO.getIsbn() != null) book.setIsbn(bookDTO.getIsbn());
            if(bookDTO.getPublishedDate() != null) book.setPublishedDate(bookDTO.getPublishedDate());
            if(bookDTO.getTotalCopies() != null) book.setTotalCopies(bookDTO.getTotalCopies());
            if(bookDTO.getAvailableCopies() != null) book.setAvailableCopies(bookDTO.getAvailableCopies());

            Book updatedBook = bookRepository.save(book);

            BookDTO updatedBookDTO = new BookDTO();
            BeanUtils.copyProperties(updatedBook,updatedBookDTO);
            return updatedBookDTO;
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

    public BookDTO getBookById(int id) {
        Book book = bookRepository.findById(id).orElse(null);

        BookDTO bookDTO = new BookDTO();

    }

    public List<BookDTO> searchBooks(String keyword) {
        return bookRepository.searchBooks(keyword);
    }
}
