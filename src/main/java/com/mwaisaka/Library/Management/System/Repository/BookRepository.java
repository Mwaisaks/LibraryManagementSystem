package com.mwaisaka.Library.Management.System.Repository;

import com.mwaisaka.Library.Management.System.domain.Dto.BookDTO;
import com.mwaisaka.Library.Management.System.domain.models.Book;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Integer> {

    List<BookDTO> searchBooks(String keyword);

    boolean existsByTitleAndAuthor(String title, String author);
}

