package com.mwaisaka.Library.Management.System.controller;

import com.mwaisaka.Library.Management.System.domain.dto.request.BorrowRequest;
import com.mwaisaka.Library.Management.System.domain.dto.request.ReturnRequest;
import com.mwaisaka.Library.Management.System.service.BorrowReturnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class BorrowReturnController {

    private final BorrowReturnService borrowReturnService;

    @PostMapping("/borrow")
    public ResponseEntity<String> borrowBook(@Valid @RequestBody BorrowRequest borrowRequest){

        String result = borrowReturnService.borrowBook(borrowRequest);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnBook(@Valid @RequestBody ReturnRequest returnRequest){
        String result = borrowReturnService.returnBook(returnRequest);
        return ResponseEntity.ok(result);
    }
}
