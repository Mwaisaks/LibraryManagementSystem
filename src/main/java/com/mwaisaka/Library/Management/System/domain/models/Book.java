package com.mwaisaka.Library.Management.System.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book")
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    private String title;
    private String author;
    private String publisher;
    private  String isbn;
    private Date publishedDate;
    private Integer totalCopies;
    private Integer availableCopies;
}
