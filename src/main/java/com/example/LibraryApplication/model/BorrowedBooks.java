package com.example.LibraryApplication.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@Entity(name="borrowed_books")
public class BorrowedBooks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "borrow_date")
    private LocalDateTime borrowDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BorrowStatus status = BorrowStatus.BORROWED;

//    public Date getDueDate() {
//
//    }

    public Instant getDueDate(LocalDateTime borrowDate, int borrowPeriod) {
        return borrowDate.plusDays(borrowPeriod)
                .atZone(ZoneId.systemDefault()) // Convert to system timezone
                .toInstant(); // Convert to Instant
    }

    public enum BorrowStatus {
        BORROWED, RETURNED
    }

//    public long borrowDuration(){
//        long duration = LocalDate.now().atTime()-borrowDate.getTime();
//        return TimeUnit.MILLISECONDS.toSeconds(duration);
//    }
}