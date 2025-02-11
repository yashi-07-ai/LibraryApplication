package com.example.Library.service;

import com.example.Library.model.BorrowedBooks;
import com.example.Library.repository.BorrowedBooksRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReminderService {
    @Autowired
    private BorrowedBooksRepository borrowedBooksRepository;

    @Autowired
    private EmailService emailService;

    @Value("${app.borrowPeriod}")
    private String borrowPeriod;

    private static Logger log = LoggerFactory.getLogger(ReminderService.class);

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
