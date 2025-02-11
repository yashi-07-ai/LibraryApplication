package com.example.Library.controller;


import com.example.Library.dto.BookDTO;
import com.example.Library.dto.BookResponseDTO;
import com.example.Library.exception.NullFieldException;
import com.example.Library.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name="Book API", description="Manage books in library")
public class bookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/search-all")
    @Operation(summary = "Get all books", description = "Retrieve all books in the library")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No books found")
    })
    public List<BookResponseDTO> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/search-id/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieve a book by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public BookResponseDTO getBookById(
            @Parameter(description = "Unique ID of the book", required = true)
            @PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/search-title/{title}")
    @Operation(summary = "Get book by Title", description = "Retrieve a book by its title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public List<BookResponseDTO> getBookByTitle(
            @Parameter(description = "Title of the book", required = true)
            @PathVariable String title) {
        return bookService.getBookByTitle(title);
    }

    @PostMapping("/addBook/{userId}")
    @Operation(summary = "Add a new book", description = "Create a new book in the library")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public BookResponseDTO addBook(@RequestBody BookDTO bookDto, @PathVariable Long userId){
        return bookService.addBook(bookDto, userId);
    }

    @PostMapping(value = "/upload/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a file", description = "Upload a file containing book data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid file")
    })
    public ResponseEntity<String> uploadFile(
            @Parameter(description = "File containing book data", required = true)
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "User ID who is uploading the file", required = true)
            @PathVariable Long userId) {
        if (file.isEmpty()) {
            throw new NullFieldException("Please upload a file");
        }
        bookService.processFile(file, userId);
        return ResponseEntity.ok("File uploaded successfully. Processing in background.");
    }

    @PutMapping("/update/{userId}")
    @Operation(summary = "Update a book", description = "Update book details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public BookResponseDTO updateBook(
            @Parameter(description = "User ID of the updater", required = true)
            @PathVariable Long userId,
            @RequestBody BookDTO updatedBook) {
        return bookService.updateBook(userId, updatedBook);
    }

    @DeleteMapping("/{bookId}/user/{userId}")
    @Operation(summary = "Delete a book", description = "Delete a book from the library")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid book or user ID")
    })
    public ResponseEntity<String> deleteBook(
            @Parameter(description = "Book ID to be deleted", required = true)
            @PathVariable Long bookId,
            @Parameter(description = "User ID of the requester", required = true)
            @PathVariable Long userId) {
        bookService.deleteBook(bookId, userId);
        return ResponseEntity.ok("Book deleted successfully");
    }
}
