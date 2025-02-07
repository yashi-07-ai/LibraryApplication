package com.example.LibraryApplication.service;

import com.example.LibraryApplication.exceptions.BookAlreadyExists;
import com.example.LibraryApplication.exceptions.NoSuchBookExistsException;
import com.example.LibraryApplication.exceptions.NoSuchUserExistsExceptions;
import com.example.LibraryApplication.model.Book;
import com.example.LibraryApplication.model.User;
import com.example.LibraryApplication.repository.BookRepository;
import com.example.LibraryApplication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public Book getBookById(Long id){
        return bookRepository.findById(id).orElseThrow(()-> new NoSuchBookExistsException(
                "No user present with ID = " + id
        ));
    }

    //@Transactional
    public Book addBook(Book book){
        if (book.getId() != null) {
            throw new IllegalArgumentException("ID should be null for new books.");
        }

        // You can also check if a book with the same title (or other unique field) exists
        Optional<Book> existingBook = bookRepository.findByIsbn(book.getTitle()); // Assuming title is unique
        if (existingBook.isPresent()) {
            throw new BookAlreadyExists("Book already exists with this title");
        }

        // Save the new book
        bookRepository.save(book);
        return book;
    }

//    public Book updateBook(int id, Book updatedBook){
//
//    }

//    public String borrowBook(int bookId, int userId){
//
//    }

//    public String returnBook(int bookId){
//
//    }

    public void deleteBook(Long bookId, Long userId){
        // Fetch the user attempting to delete the book
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchUserExistsExceptions("User not found"));

        // Check if the user is an ADMIN
        if (user.getRole() != User.UserRole.ADMIN) {
            throw new RuntimeException("Only ADMIN users can delete books");
        }

        // Fetch the book to delete
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchBookExistsException("Book not found"));

        // Delete the book
        bookRepository.delete(book);
    }
}
