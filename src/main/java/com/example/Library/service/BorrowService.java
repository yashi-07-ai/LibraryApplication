package com.example.Library.service;

import com.example.Library.dto.BookDTO;
import com.example.Library.exception.NoSuchBookExistsException;
import com.example.Library.exception.NoSuchUserExistsException;
import com.example.Library.exception.NotBorrowedException;
import com.example.Library.model.Book;
import com.example.Library.model.BorrowedBooks;
import com.example.Library.model.User;
import com.example.Library.repository.BookRepository;
import com.example.Library.repository.BorrowedBooksRepository;
import com.example.Library.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    private BookDTO convertToBookDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setAvailable(book.isAvailable());
        return bookDTO;
    }

    private static Logger log = LoggerFactory.getLogger(BorrowService.class);


    @Transactional
    public BorrowedBooks borrowBook(Long userId, Long bookId) {
        log.info("Attempting to borrow book with ID: {}", bookId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchUserExistsException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchBookExistsException("Book not found"));

        // Check if the book is already borrowed
        boolean isBorrowed = borrowedBooksRepository.existsByBookIdAndStatus(bookId, BorrowedBooks.BorrowStatus.BORROWED);

        if (isBorrowed) {
            throw new IllegalStateException("Book is already borrowed.");
        }

        // Mark the book as unavailable
        book.setAvailable(false);
        bookRepository.save(book);

        // Create a new borrowed record
        BorrowedBooks borrowedBook = new BorrowedBooks();
        borrowedBook.setUser(user);
        borrowedBook.setBook(book);
        borrowedBook.setBorrowDate(LocalDateTime.now());
        borrowedBook.setStatus(BorrowedBooks.BorrowStatus.BORROWED);

        borrowedBooksRepository.save(borrowedBook);
        log.info("Book {} borrowed by user {}", book.getTitle(), user.getName());

        return borrowedBook;
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
