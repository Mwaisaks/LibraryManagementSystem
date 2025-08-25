package com.mwaisaka.Library.Management.System.controller;


import com.mwaisaka.Library.Management.System.domain.dto.response.ApiResult;
import com.mwaisaka.Library.Management.System.service.BookService;
import com.mwaisaka.Library.Management.System.domain.dto.BookDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/books")
@RequiredArgsConstructor

@Tag(
        name = "Books",
        description = "Endpoints for managing books in the library"
)
public class BookController {

    private final BookService bookService;

    @Operation(summary = "Add a new book")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid book data",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    @PostMapping("/add")
    public ResponseEntity<ApiResult<BookDTO>> addBook(@Valid @RequestBody BookDTO bookDTO) {
        BookDTO createdBook =  bookService.addBook(bookDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResult.success("Book created successfully", createdBook));
    }

    @Operation(summary = "Retrieve all books")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResult<List<BookDTO>>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(ApiResult.success("Books found", books));
    }

    @Operation(summary = "Retrieve a book by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "204", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
   @GetMapping("/{bookId}")
   public ResponseEntity<ApiResult<BookDTO>> getBokById(@PathVariable int bookId) {
        BookDTO book = bookService.getBookById(bookId);

        if(book != null) {
            return ResponseEntity.ok(ApiResult.success("Book found", book));
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResult.error("Book not found"));
        }
   }

    @Operation(summary = "Update an existing book")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book updated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid book update data",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
   @PutMapping("/{id}")
   public ResponseEntity<ApiResult<BookDTO>> updateBook(@PathVariable Integer id,@Valid @RequestBody BookDTO bookDTO) {
        BookDTO updateBook = bookService.updateBook(id, bookDTO);

        if (updateBook != null) {
            return ResponseEntity.ok(ApiResult.success("Book updated successfully", updateBook));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResult.error("Book not found"));
        }
   }

    @Operation(summary = "Search for books by keyword (title, author, etc.)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search results retrieved",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<ApiResult<List<BookDTO>>> searchBooks(@RequestParam String keyword) {
        List<BookDTO> books = bookService.searchBooks(keyword);
        return ResponseEntity.ok(ApiResult.success("Search results", books));
    }

    @Operation(summary = "Delete a book by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book deleted successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "204", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> deleteBook(@PathVariable Integer id) {
        boolean deleted = bookService.deleteBook(id);

        if (deleted) {
            return ResponseEntity.ok(ApiResult.success("Book deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResult.error("Book not found"));
        }
    }
}
