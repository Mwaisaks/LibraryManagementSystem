package com.mwaisaka.Library.Management.System.controller;


import com.mwaisaka.Library.Management.System.Dto.request.BookRequest;
import com.mwaisaka.Library.Management.System.Service.BookService;
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
    public ResponseEntity<BookRequest> createBook(@Valid @RequestBody BookRequest bookRequest){

        BookRequest createdBook = bookService.createBook(bookRequest);

        return  new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BookRequest>> getAllBooks(){

        List<BookRequest> books= bookService.getAllBooks();

        return new ResponseEntity<>(books,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookRequest> getBookById(@PathVariable int id){

        BookRequest book = bookService.getBookById(id);

        if (book != null)
            return new ResponseEntity<>(book, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookRequest> updateBook(@PathVariable Integer id, @Valid @RequestBody BookRequest bookRequest){

        BookRequest updatedBook = bookService.updateBook(id, bookRequest);

        if(updatedBook != null){
            return new ResponseEntity<>(updatedBook,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookRequest>> searchBooks(@RequestParam String keyword){
        List<BookRequest> bookRequests = bookService.searchBooks(keyword);
        return new ResponseEntity<>(bookRequests, HttpStatus.OK);
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

