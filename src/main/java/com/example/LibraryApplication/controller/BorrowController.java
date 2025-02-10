package com.example.LibraryApplication.controller;

import com.example.LibraryApplication.dto.BookDTO;
import com.example.LibraryApplication.model.Book;
import com.example.LibraryApplication.model.BorrowedBooks;
import com.example.LibraryApplication.repository.BorrowedBooksRepository;
import com.example.LibraryApplication.service.BorrowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/borrow")
public class BorrowController {
    @Autowired
    private BorrowService borrowService;

    @Autowired
    private BorrowedBooksRepository borrowedBooksRepository;

    @PostMapping("/borrowBook/{userId}/{bookId}")
    public ResponseEntity<String> borrowBook(@PathVariable Long userId, @PathVariable Long bookId) {
        borrowService.borrowBook(userId, bookId);
        return ResponseEntity.ok("Book borrowed");
    }

    @PostMapping("/returnBook/{borrowId}")
    public ResponseEntity<String> returnBook(@PathVariable Long borrowId) {
        borrowService.returnBook(borrowId);
        return ResponseEntity.ok("Book returned");
    }

    @GetMapping("/all")
    public List<BookDTO> getAllBooks(){
        List<BookDTO> books = borrowService.getAllBorrowedBooks();
        return ResponseEntity.ok(books).getBody();
    }
}
