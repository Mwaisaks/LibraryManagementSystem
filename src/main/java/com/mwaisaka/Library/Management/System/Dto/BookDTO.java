package com.mwaisaka.Library.Management.System.Dto;


import lombok.Data;

import java.util.Date;

@Data
public class BookDTO {
    private  Integer id;
    private  String title;
    private  String publisher;
    private String isbn;
    private Date publishedDate;
    private Integer totalCopies;
    private Integer availableCopies;
}

