package com.example.LibraryApplication.repository;

import com.example.LibraryApplication.model.BorrowedBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BorrowedBooksRepository extends JpaRepository<BorrowedBooks, Long> {
    List<BorrowedBooks> findByStatus(BorrowedBooks.BorrowStatus borrowStatus);

//    @Query("SELECT b FROM BorrowedBooks b WHERE b.status = 'BORROWED' AND b.returnDate IS NULL AND b.dueDate < CURRENT_DATE")
//    List<BorrowedBooks> findOverdueBooks();

}