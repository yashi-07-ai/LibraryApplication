package com.example.Library.controller;


import com.example.Library.dto.BookDTO;
import com.example.Library.dto.BookResponseDTO;
import com.example.Library.exception.NullFieldException;
import com.example.Library.service.BookService;
import io.github.bucket4j.Bucket;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name="Book API", description="Manage books in library")
public class bookController {

    @Autowired
    private BookService bookService;

    private final Bucket bucket;

    @Autowired
    private AuthenticationManager authenticationManager;

    public bookController(Bucket bucket) {
        this.bucket = bucket;
    }

    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/")
    public String welcome(){
        return "welcome";
    }

    // Any authenticated user can search books
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @GetMapping("/search-all")
    public List<BookResponseDTO> getAllBooks() {
        if(bucket.tryConsume(1)){
            return bookService.getAllBooks();
        }
        throw new RuntimeException("try in some time, limit reached");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @GetMapping("/search-id/{id}")
    public BookResponseDTO getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    //    @PreAuthorize("hasAuthority('ADMIN')")
//    @GetMapping("/detailsByISBN/{isbn}")
//    public Mono<String> getBookDetails(@PathVariable String isbn) {
//        return openLibraryService.getBookDetailsByIsbn(isbn);
//    }

    @PostConstruct
    public void triggerAsyncTaskOnStartup() {
        System.out.println("Starting async task on application startup...");
        bookService.processInBackground(1L);  // Trigger async task for book ID 1
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @GetMapping("/search-title/{title}")
    public List<BookResponseDTO> getBookByTitle(@PathVariable String title) {
        return bookService.getBookByTitle(title);
    }

    // Only Admins can add books
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addBook/{userId}")
    public BookResponseDTO addBook(@RequestBody BookDTO bookDto, @PathVariable Long userId) {
        return bookService.addBook(bookDto, userId);
    }

    // Only Admins can upload files
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/upload/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable Long userId) {
        if (file.isEmpty()) {
            throw new NullFieldException("Please upload a file");
        }
        bookService.processFile(file, userId);
        return ResponseEntity.ok("File uploaded successfully. Processing in background.");
    }

    @PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
    @PostMapping("/updateAvailability")
    public Mono<String> updateBookAvailability(@RequestParam String bookTitle, @RequestParam boolean isAvailable) {
        return bookService.updateBookAvailability(bookTitle, isAvailable)
                .thenReturn("Updated availability for " + bookTitle);
    }

    // Only Admins can update books
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{userId}")
    public BookResponseDTO updateBook(@PathVariable Long userId, @RequestBody BookDTO updatedBook) {
        return bookService.updateBook(userId, updatedBook);
    }

    // Only Admins can delete books
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{bookId}/user/{userId}")
    public ResponseEntity<String> deleteBook(@PathVariable Long bookId, @PathVariable Long userId) {
        bookService.deleteBook(bookId, userId);
        return ResponseEntity.ok("Book deleted successfully");
    }
}
