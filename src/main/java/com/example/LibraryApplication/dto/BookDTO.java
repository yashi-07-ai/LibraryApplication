package com.example.LibraryApplication.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {
    @NotBlank(message = "Title is required")
    @Size(min=3, max=100)
    private String title;

    @Size(min=3, max=100)
    private String author;

    @NotBlank(message = "Isbn is required")
    @Size(min=4)
    private String isbn;

    @NotNull()
    private boolean available = true;

}
