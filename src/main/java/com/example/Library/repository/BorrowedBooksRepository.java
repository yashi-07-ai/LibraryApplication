package com.example.Library.repository;

import com.example.Library.model.BorrowedBooks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowedBooksRepository extends JpaRepository<BorrowedBooks, Long> {
    List<BorrowedBooks> findByStatus(BorrowedBooks.BorrowStatus borrowStatus);
    boolean existsByBookIdAndStatus(Long bookId, BorrowedBooks.BorrowStatus status);

//    @Query("SELECT b FROM BorrowedBooks b WHERE b.status = 'BORROWED' AND b.returnDate IS NULL AND b.dueDate < CURRENT_DATE")
//    List<BorrowedBooks> findOverdueBooks();

}