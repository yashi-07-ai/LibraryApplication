package com.example.LibraryApplication.controller;

import com.example.LibraryApplication.exceptions.UserAlreadyExists;
import com.example.LibraryApplication.exceptions.ErrorResponse;
import com.example.LibraryApplication.model.Book;
import com.example.LibraryApplication.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getAllBooks(){
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id){
        Book book = bookService.getBookById(id);
        return book != null ? ResponseEntity.ok(book) : ResponseEntity.notFound().build();
    }

    @PostMapping("/addBook")
    public Book addBook(@RequestBody Book book){
        return bookService.addBook(book);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Book> updateBook(@PathVariable int id, @RequestBody Book book){
//        Book updatedBook = bookService.updateBook(id, book);
//        return updatedBook != null ? ResponseEntity.ok(updatedBook) : ResponseEntity.notFound().build();
//    }

//    @PostMapping("/{bookId}/borrow/{userId}")
//    public ResponseEntity<String> borrowBook(@PathVariable int bookId, @PathVariable int userId){
//        String message = bookService.borrowBook(bookId, userId);
//        return ResponseEntity.ok(message);
//    }

//    @PostMapping("/return/{bookId}")
//    public ResponseEntity<String> returnBook(@PathVariable int bookId){
//        String message = bookService.returnBook(bookId);
//        return ResponseEntity.ok(message);
//    }

    @DeleteMapping("/{bookId}/delete/{userId}")
    public ResponseEntity<String> deleteBook(@PathVariable Long bookId, @PathVariable Long userId) {
        bookService.deleteBook(bookId, userId);
        return ResponseEntity.ok("Book deleted successfully");
    }

    @ExceptionHandler(value = UserAlreadyExists.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleCustomerAlreadyExistsException(UserAlreadyExists ex) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
    }
}
