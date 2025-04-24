package com.mwaisaka.Library.Management.System.Dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class BookDTO {
    private  Integer id;
    @Size(min = 2, max = 20, message = "Title must be between 2 and 20 characters")
    @NotEmpty(message = "Title is required")

    private  String title;
    private String author;
    private String email;
    private  String publisher;
    private String isbn;
    private Date publishedDate;
    private Integer totalCopies;
    private Integer availableCopies;

}

