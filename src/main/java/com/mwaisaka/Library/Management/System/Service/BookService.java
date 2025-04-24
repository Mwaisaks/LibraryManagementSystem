package com.mwaisaka.Library.Management.System.Service;

import com.mwaisaka.Library.Management.System.Repository.BookRepository;
import com.mwaisaka.Library.Management.System.mapper.BookMapper;
import com.mwaisaka.Library.Management.System.models.Book;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    //private  final ModelMapper modelMapper;
    private final BookMapper bookMapper;

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
