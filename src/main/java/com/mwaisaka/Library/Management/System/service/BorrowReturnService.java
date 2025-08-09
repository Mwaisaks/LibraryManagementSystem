package com.mwaisaka.Library.Management.System.service;


import com.mwaisaka.Library.Management.System.domain.dto.request.BorrowRequest;
import com.mwaisaka.Library.Management.System.domain.dto.request.ReturnRequest;
import org.springframework.stereotype.Service;

@Service
public interface BorrowReturnService {

    String borrowBook(BorrowRequest borrowRequest);
    String returnBook(ReturnRequest returnRequest);

}

