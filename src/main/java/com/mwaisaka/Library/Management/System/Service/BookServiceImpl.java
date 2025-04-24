package com.mwaisaka.Library.Management.System.Service;

import com.mwaisaka.Library.Management.System.domain.Dto.BookDTO;
import com.mwaisaka.Library.Management.System.Repository.BookRepository;
import com.mwaisaka.Library.Management.System.domain.mappers.BookMapper;
import com.mwaisaka.Library.Management.System.domain.models.Book;
import com.mwaisaka.Library.Management.System.exceptions.BookNotFoundException;
import com.mwaisaka.Library.Management.System.exceptions.ResourceAlreadyExists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Transactional
    public BookDTO createBook(BookDTO bookDTO){
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
    public BookDTO updateBook(Integer id,BookDTO bookDTO){
        Optional<Book> bookOptional = bookRepository.findById(id);
        if(bookOptional.isPresent()){
            Book book = bookOptional.get();

            Book updatedBook = bookRepository.save(book);

            BookDTO updatedBookDTO = new BookDTO();
            BeanUtils.copyProperties(updatedBook,updatedBookDTO);
            return updatedBookDTO;
        }
        return null;
    }
    public boolean deleteBook(int id){
        Optional<Book> bookOptional = bookRepository.findById(id);
        if(bookOptional.isPresent()){
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public BookDTO getBookById(int bookId) {
       if(bookId <= 0){
           throw new IllegalArgumentException("Invalid book id");
       }

       Book book = bookRepository.findById(bookId).orElseThrow(() ->
               new BookNotFoundException("Book not found with id: " + bookId));
       return bookMapper.toDto(book);
    }
    public List<BookDTO> searchBooks(String keyword) {
        return bookRepository.searchBooks(keyword);
    }
}
