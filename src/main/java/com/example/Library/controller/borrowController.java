package com.example.Library.controller;

import com.example.Library.dto.BookDTO;
import com.example.Library.repository.BorrowedBooksRepository;
import com.example.Library.service.BorrowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow")
@Tag(name="Borrow API", description="Borrow and return books in library")
public class borrowController {
    @Autowired
    private BorrowService borrowService;

    @Autowired
    private BorrowedBooksRepository borrowedBooksRepository;

    @PostMapping("/borrowBook/{userId}/{bookId}")
    @Operation(summary = "Borrow book", description = "Borrow book from library")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books borrowed successfully"),
            @ApiResponse(responseCode = "404", description = "Book not borrowed")
    })
    public ResponseEntity<String> borrowBook(@PathVariable Long userId, @PathVariable Long bookId) {
        borrowService.borrowBook(userId, bookId);
        return ResponseEntity.ok("Book borrowed");
    }

    @PostMapping("/returnBook/{borrowId}")
    @Operation(summary = "Return book", description = "Return book to library")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books returned successfully"),
            @ApiResponse(responseCode = "404", description = "Book not returned")
    })
    public ResponseEntity<String> returnBook(@PathVariable Long borrowId) {
        borrowService.returnBook(borrowId);
        return ResponseEntity.ok("Book returned");
    }

    @GetMapping("/all")
    @Operation(summary = "Get all books", description = "Retrieve all the borrowed books in the library")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public List<BookDTO> getAllBooks(){
        List<BookDTO> books = borrowService.getAllBorrowedBooks();
        return ResponseEntity.ok(books).getBody();
    }
}
