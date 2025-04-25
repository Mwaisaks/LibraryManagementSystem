package com.mwaisaka.Library.Management.System.domain.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book")
@Getter
@Setter
@Builder
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
