package com.mwaisaka.Library.Management.System.controller;

import com.mwaisaka.Library.Management.System.domain.dto.request.BorrowRequest;
import com.mwaisaka.Library.Management.System.domain.dto.request.ReturnRequest;
import com.mwaisaka.Library.Management.System.domain.dto.response.ApiResult;
import com.mwaisaka.Library.Management.System.service.BorrowReturnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
@Tag(
        name = "Borrow-Return",
        description = "Endpoints for managing borrowing and returning of  books in the library"
)
public class BorrowReturnController {


    private final BorrowReturnService borrowReturnService;

    public BorrowReturnController(BorrowReturnService borrowReturnService) {
        this.borrowReturnService = borrowReturnService;
    }

    @Operation(
            summary = "Borrow a book",
            description = "Allows a user to borrow a book by providing member ID and book ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book borrowed successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request or book not available",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    @PostMapping("/borrow")
    public ResponseEntity<ApiResult<String>> borrowBook(@Valid @RequestBody BorrowRequest borrowRequest) {
        try{
            String result = borrowReturnService.borrowBook(borrowRequest);
            return ResponseEntity.ok(ApiResult.success("Book borrow successfully", result));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResult.error(e.getMessage()));
        }
    }

    @Operation(
            summary = "Return a book",
            description = "Allows a user to return a borrowed book by providing member ID and book ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book returned successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request or book not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    @PostMapping("/return")
    public ResponseEntity<ApiResult<String>> returnBook(@Valid @RequestBody ReturnRequest borrowRequest) {
        try{
            String result = borrowReturnService.returnBook(borrowRequest);
            return ResponseEntity.ok(ApiResult.success("Return book successfully", result));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResult.error(e.getMessage()));
        }
    }
}
