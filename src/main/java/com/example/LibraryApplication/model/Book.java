package com.example.LibraryApplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="book_id")
    private Long id;

    @Column(name="title")
    @NotNull
    private String title;

    @Column(name = "author")
    @NotNull
    private String author;

    @NotNull
    @Column(name="isbn")
    private String isbn;

    @Column(name = "status")
    private boolean available = true;
}
