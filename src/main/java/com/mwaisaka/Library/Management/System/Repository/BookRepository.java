package com.mwaisaka.Library.Management.System.Repository;

import com.mwaisaka.Library.Management.System.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Integer> {

}

