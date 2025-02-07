package com.example.LibraryApplication.service;

import com.example.LibraryApplication.exceptions.NoSuchBookExistsException;
import com.example.LibraryApplication.exceptions.NotBorrowedException;
import com.example.LibraryApplication.model.Book;
import com.example.LibraryApplication.model.BorrowedBooks;
import com.example.LibraryApplication.model.User;
import com.example.LibraryApplication.repository.BookRepository;
import com.example.LibraryApplication.repository.BorrowedBooksRepository;
import com.example.LibraryApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowService {
    @Autowired
    private BorrowedBooksRepository borrowedBooksRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public BorrowedBooks borrowBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchBookExistsException("User not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NoSuchBookExistsException("Book not found"));

        if (!book.isAvailable()) {
            throw new RuntimeException("Book is already borrowed");
        }

        BorrowedBooks borrowedBook = new BorrowedBooks();
        borrowedBook.setUser(user);
        borrowedBook.setBook(book);
        borrowedBook.setBorrowDate(LocalDateTime.now());
        borrowedBook.setStatus(BorrowedBooks.BorrowStatus.BORROWED);

        book.setAvailable(false);
        bookRepository.save(book);

        return borrowedBooksRepository.save(borrowedBook);
    }

    @Transactional
    public BorrowedBooks returnBook(Long borrowId) {
        BorrowedBooks borrowedBook = borrowedBooksRepository.findById(borrowId)
                .orElseThrow(() -> new NotBorrowedException("Borrow record not found"));

        borrowedBook.setReturnDate(LocalDateTime.now());
        borrowedBook.setStatus(BorrowedBooks.BorrowStatus.RETURNED);

        Book book = borrowedBook.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        return borrowedBooksRepository.save(borrowedBook);
    }

    public List<Book> getAllBorrowedBooks() {
        // Find all BorrowedBooks records with status = BORROWED
        List<BorrowedBooks> borrowedBooksList = borrowedBooksRepository.findByStatus(BorrowedBooks.BorrowStatus.BORROWED);

        // Extract the Book objects from the BorrowedBooks records
        return borrowedBooksList.stream()
                .map(BorrowedBooks::getBook) // Extract the Book object
                .collect(Collectors.toList()); // Convert to a List<Book>
    }
}
