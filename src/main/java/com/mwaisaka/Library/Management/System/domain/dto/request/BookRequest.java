package com.mwaisaka.Library.Management.System.domain.dto.request;

import com.mwaisaka.Library.Management.System.domain.enums.BookGenre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class BookRequest {

    @NotBlank
    private String title;

    private String author;
    private String publisher;
    private String isbn;
    private String publishedDate;

    @PositiveOrZero
    private Integer totalCopies;
    private BookGenre genre;

}

//I don't understand the request and response concept
//How do I make sure that I don't rely heavily on AI
