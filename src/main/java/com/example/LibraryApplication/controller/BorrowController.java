package com.example.LibraryApplication.controller;

import com.example.LibraryApplication.model.Book;
import com.example.LibraryApplication.model.BorrowedBooks;
import com.example.LibraryApplication.repository.BorrowedBooksRepository;
import com.example.LibraryApplication.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {
    @Autowired
    private BorrowService borrowService;

    @Autowired
    private BorrowedBooksRepository borrowedBooksRepository;

    @PostMapping("/borrowBook/{userId}/{bookId}")
    public BorrowedBooks borrowBook(@PathVariable Long userId, @PathVariable Long bookId) {
        return borrowService.borrowBook(userId, bookId);
    }

    @PostMapping("/returnBook/{borrowId}")
    public BorrowedBooks returnBook(@PathVariable Long borrowId) {
        return borrowService.returnBook(borrowId);
    }

    @GetMapping("/all")
    public List<Book> getAllBooks(){
        return borrowService.getAllBorrowedBooks();
    }
}
