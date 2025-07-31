package com.mwaisaka.Library.Management.System.domain.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book")
@Getter
@Setter
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private Date publishedDate;
    private Integer totalCopies;
    private Integer availableCopies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrowed_books")
    private User borrowedBy;
}
