package com.mwaisaka.Library.Management.System.domain.dto.response;

import com.mwaisaka.Library.Management.System.domain.enums.BookGenre;
import com.mwaisaka.Library.Management.System.domain.enums.BookStatus;

import java.util.UUID;

public class BookResponse {

    private UUID id;
    private String title;
    private String author;
    private String isbn;
    private Integer availableCopies;
    private BookStatus status;
    private BookGenre genre;
}
