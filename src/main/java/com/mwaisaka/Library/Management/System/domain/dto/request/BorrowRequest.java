package com.mwaisaka.Library.Management.System.domain.dto.request;

import java.time.LocalDate;

public class BorrowRequest {

    private Long userId;
    private Integer bookId;
    private LocalDate dueDate; //putting a default for 14 days?
}
