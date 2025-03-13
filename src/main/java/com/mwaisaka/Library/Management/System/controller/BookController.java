package com.mwaisaka.Library.Management.System.controller;


import com.mwaisaka.Library.Management.System.Dto.BookDTO;
import com.mwaisaka.Library.Management.System.Service.BookService;
import com.mwaisaka.Library.Management.System.models.Book;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/add")
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO){

        BookDTO createdBook = bookService.createBook(bookDTO);

        return  new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks(){

        List<BookDTO> books= bookService.getAllBooks();

        return new ResponseEntity<>(books,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<BookDTO> getBookById(@PathVariable int id){

        BookDTO book = bookService.getBookById(id);

        if (book != null)
            return new ResponseEntity<>(book, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Integer id,@Valid @RequestBody BookDTO bookDTO){

        BookDTO updatedBook = bookService.updateBook(id,bookDTO);

        if(updatedBook != null){
            return new ResponseEntity<>(updatedBook,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/books/search")
    public ResponseEntity<List<BookDTO>> searchBooks(@RequestParam String keyword){
        List<BookDTO> bookDTOS = bookService.searchBooks(keyword);
        return new ResponseEntity<>(bookDTOS, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id){

        boolean deleted = bookService.deleteBook(id);

        if (deleted){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
