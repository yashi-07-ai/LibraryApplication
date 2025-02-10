package com.example.LibraryApplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "books")
public class Book {
    @Getter
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
    @Column(name="isbn", unique = true)
    private String isbn;

    @Column(name = "status")
    private boolean available = true;

    public boolean getAvailable() {
        return available;
    }

}
