package com.example.LibraryApplication.dto;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {


    @NotBlank(message = "Title is required")
    @Size(min=3, max=100)
    @Schema(description = "Title of the book", example = "Effective Java")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Size(min=3, max=100)
    @Schema(description = "Author of the book", example = "Joshua Bloch")
    private String author;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }



    @NotBlank(message = "Isbn is required")
    @Size(min=4)
    @Schema(description = "Book ISBN", example = "1234")
    private String isbn;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }


    @Schema(description = "Book Availability", example = "true")
    private boolean available;

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

//    public BookDTO() {
//    }

//    public BookDTO(String title, String author, String isbn, boolean available) {
//        this.title = title;
//        this.author = author;
//        this.isbn = isbn;
//        this.available = available;
//    }

}
