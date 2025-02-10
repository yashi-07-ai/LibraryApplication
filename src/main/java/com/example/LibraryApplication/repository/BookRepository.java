package com.example.LibraryApplication.repository;

import com.example.LibraryApplication.model.Book;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(@NotNull String isbn);

    @Query("SELECT b FROM Book b WHERE b.title ILIKE %:keyword%")
    Optional<List<Book>> findByTitle(@Param("keyword") String keyword);
}
