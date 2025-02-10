package com.example.LibraryApplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
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
}
