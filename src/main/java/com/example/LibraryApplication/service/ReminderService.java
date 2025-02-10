package com.example.LibraryApplication.service;

import com.example.LibraryApplication.model.BorrowedBooks;
import com.example.LibraryApplication.repository.BorrowedBooksRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReminderService {
    @Autowired
    private BorrowedBooksRepository borrowedBooksRepository;

    @Autowired
    private EmailService emailService;

    @Value("${app.borrowPeriod}")
    private String borrowPeriod;

    @Async
    @Scheduled(cron = "0 0 9 * * ?")
    public void reminder(){
        List<BorrowedBooks> overdueBooks = new ArrayList<>();
        for (BorrowedBooks borrow : borrowedBooksRepository.findAll()) {
            if (borrow.getStatus() == BorrowedBooks.BorrowStatus.BORROWED
                    && borrow.getReturnDate() == null
                    && borrow.getDueDate(borrow.getBorrowDate(), Integer.parseInt(borrowPeriod)).isBefore(Instant.now())) {
                overdueBooks.add(borrow);
            }
        }

        for(BorrowedBooks books : overdueBooks){
            log.info("Sending email for overdueBooks");
            emailService.sendReminderEmailAsync(books);
        }
    }

}
