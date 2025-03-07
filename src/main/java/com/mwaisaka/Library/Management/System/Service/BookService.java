package com.mwaisaka.Library.Management.System.Service;

import com.mwaisaka.Library.Management.System.Dto.BookDTO;
import com.mwaisaka.Library.Management.System.Repository.BookRepository;
import com.mwaisaka.Library.Management.System.models.Book;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
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

}
