package com.mwaisaka.Library.Management.System.domain.dto.response;

import com.mwaisaka.Library.Management.System.domain.enums.BookGenre;
import com.mwaisaka.Library.Management.System.domain.enums.BookStatus;

public class BookResponse {

    private Integer id;
    private String title;
    private String author;
    private String isbn;
    private Integer availableCopies;
    private BookStatus status;
    private BookGenre genre;
}
