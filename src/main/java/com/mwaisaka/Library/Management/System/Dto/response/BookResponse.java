package com.mwaisaka.Library.Management.System.Dto.response;

import com.mwaisaka.Library.Management.System.enums.BookGenre;
import com.mwaisaka.Library.Management.System.enums.BookStatus;

public class BookResponse {

    private Integer id;
    private String title;
    private String author;
    private String isbn;
    private Integer availableCopies;
    private BookStatus status;
    private BookGenre genre;
}
