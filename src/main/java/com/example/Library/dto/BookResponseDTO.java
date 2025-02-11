package com.example.Library.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDTO {

    @NotNull
    @Schema(description = "Book ID", example = "1")
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min=3, max=100)
    @Schema(description = "Book Title", example = "Ikigai")
    private String title;

    @Size(min=3, max=100)
    @Schema(description = "Book Author", example = "Hector")
    private String author;

    @NotBlank(message = "Isbn is required")
    @Size(min=4)
    @Schema(description = "Book ISBN", example = "1234")
    private String isbn;

    @Schema(description = "Book Availability", example = "true")
    private boolean available;

    public void setAvailable(boolean val){
        this.available = val;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public boolean isAvailable() {
        return available;
    }
}
