package com.mwaisaka.Library.Management.System.controller;


import com.mwaisaka.Library.Management.System.Dto.BookDTO;
import com.mwaisaka.Library.Management.System.Service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("'api/books")
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
}
