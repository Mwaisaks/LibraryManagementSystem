package com.mwaisaka.Library.Management.System.controller;


import com.mwaisaka.Library.Management.System.domain.dto.response.ApiResponse;
import com.mwaisaka.Library.Management.System.service.BookService;
import com.mwaisaka.Library.Management.System.domain.dto.BookDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<BookDTO>> addBook(@Valid @RequestBody BookDTO bookDTO) {
        BookDTO createdBook =  bookService.addBook(bookDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Book created successfully", createdBook));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookDTO>>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(ApiResponse.success("Books found", books));
    }

   @GetMapping("/{bookId}")
   public ResponseEntity<ApiResponse<BookDTO>> getBokById(@PathVariable int bookId) {
        BookDTO book = bookService.getBookById(bookId);

        if(book != null) {
            return ResponseEntity.ok(ApiResponse.success("Book found", book));
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.error("Book not found"));
        }
   }

   @PutMapping("/{id}")
   public ResponseEntity<ApiResponse<BookDTO>> updateBook(@PathVariable Integer id,@Valid @RequestBody BookDTO bookDTO) {
        BookDTO updateBook = bookService.updateBook(id, bookDTO);

        if (updateBook != null) {
            return ResponseEntity.ok(ApiResponse.success("Book updated successfully", updateBook));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Book not found"));
        }
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
