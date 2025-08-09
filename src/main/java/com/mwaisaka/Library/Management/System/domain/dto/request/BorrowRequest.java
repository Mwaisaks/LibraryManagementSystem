package com.mwaisaka.Library.Management.System.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class BorrowRequest {

    private Long userId;
    private UUID bookId;
    private LocalDate dueDate; //putting a default for 14 days?
}
