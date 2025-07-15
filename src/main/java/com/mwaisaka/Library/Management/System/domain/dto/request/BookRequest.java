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

