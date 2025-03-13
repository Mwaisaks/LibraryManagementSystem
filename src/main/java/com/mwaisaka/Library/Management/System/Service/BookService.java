package com.mwaisaka.Library.Management.System.Service;

import com.mwaisaka.Library.Management.System.Dto.BookDTO;
import com.mwaisaka.Library.Management.System.Repository.BookRepository;
import com.mwaisaka.Library.Management.System.mapper.BookMapper;
import com.mwaisaka.Library.Management.System.models.Book;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
    // MODEL MAPPERS

//
//    @Autowired
//    public BookService(BookRepository bookRepository,ModelMapper modelMapper){
//        this.bookRepository = bookRepository;
//        this.modelMapper = modelMapper;
//    }
//    public BookDTO updateBook(Integer id,BookDTO  bookDTO){
//        return bookRepository.findById(id)
//                .map(existingBook -> {
//                    modelMapper.map(bookDTO,existingBook);
//
//                    Book savedBook= bookRepository.save(existingBook);
//
//                    return modelMapper.map(savedBook,BookDTO.class);
//                }).orElse(null);
//    }


    // MAP STRUCT
    @Autowired
    public BookService(BookRepository bookRepository, BookMapper bookMapper){
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public BookDTO updateBook(Integer id,BookDTO bookDTO){
        return bookRepository.findById(id)
                .map(existingBook -> {
                    bookMapper.updateBookFromDto(bookDTO,existingBook);

                    Book savedBook = bookRepository.save(existingBook);

                    return bookMapper.toDto(savedBook);
                }).orElse(null);
    }




    public boolean deleteBook(Integer id){
        Optional<Book> bookOptional = bookRepository.findById(id);
        if(bookOptional.isPresent()){
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
