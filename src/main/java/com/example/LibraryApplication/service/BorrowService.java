package com.example.LibraryApplication.service;

import com.example.LibraryApplication.dto.BookDTO;
import com.example.LibraryApplication.exceptions.NoSuchBookExistsException;
import com.example.LibraryApplication.exceptions.NoSuchUserExistsExceptions;
import com.example.LibraryApplication.exceptions.NotBorrowedException;
import com.example.LibraryApplication.model.Book;
import com.example.LibraryApplication.model.BorrowedBooks;
import com.example.LibraryApplication.model.User;
import com.example.LibraryApplication.repository.BookRepository;
import com.example.LibraryApplication.repository.BorrowedBooksRepository;
import com.example.LibraryApplication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BorrowService {
    @Autowired
    private BorrowedBooksRepository borrowedBooksRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    private BookDTO convertToBookDTO(Book book) {
        return new BookDTO(
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getAvailable()
        );
    }

    @Transactional
    public BorrowedBooks borrowBook(Long userId, Long bookId) {
        log.info("Received request to borrow book with ID : {} by user with ID : {}", bookId, userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchUserExistsExceptions("User not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NoSuchBookExistsException("Book not found"));

        if (!book.getAvailable()) {
            throw new RuntimeException("Book is already borrowed");
        }

        BorrowedBooks borrowedBook = new BorrowedBooks();
        borrowedBook.setUser(user);
        borrowedBook.setBook(book);
        borrowedBook.setBorrowDate(LocalDateTime.now());
        borrowedBook.setStatus(BorrowedBooks.BorrowStatus.BORROWED);

        book.setAvailable(false);
        bookRepository.save(book);
        log.info("Book borrowed by the user successfully");

        return borrowedBooksRepository.save(borrowedBook);
    }

    @Transactional
    public BorrowedBooks returnBook(Long borrowId) {
        log.info("Received request to return book");
        BorrowedBooks borrowedBook = borrowedBooksRepository.findById(borrowId)
                .orElseThrow(() -> new NotBorrowedException("Borrow record not found"));

        borrowedBook.setReturnDate(LocalDateTime.now());
        borrowedBook.setStatus(BorrowedBooks.BorrowStatus.RETURNED);

        Book book = borrowedBook.getBook();
        book.setAvailable(true);
        bookRepository.save(book);
        log.info("Book returned by the user successfully");

        return borrowedBooksRepository.save(borrowedBook);
    }

    public List<BookDTO> getAllBorrowedBooks() {
        log.info("Received request to display all the borrowed books");

        // Find all BorrowedBooks records with status = BORROWED
        List<BorrowedBooks> borrowedBooksList = borrowedBooksRepository.findByStatus(BorrowedBooks.BorrowStatus.BORROWED);

        log.info("Displaying all the borrowed books in the library");
        // Extract the Book objects from the BorrowedBooks records
        return borrowedBooksList.stream()
                .map(BorrowedBooks::getBook) // Extract the Book object
                .map(this::convertToBookDTO) // Convert Book to BookDTO
                .collect(Collectors.toList()); // Convert to a List<BookDTO>
    }
}
