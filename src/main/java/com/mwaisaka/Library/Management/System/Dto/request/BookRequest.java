package com.mwaisaka.Library.Management.System.Dto.request;

import com.mwaisaka.Library.Management.System.enums.BookGenre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BookRequest {

    @NotBlank
    private String title;

    private String author;
    private String publisher;
    private String isbn;
    private Date publishedDate;

    @PositiveOrZero
    private Integer totalCopies;
    private Integer availableCopies;
    private BookGenre genre;

}


