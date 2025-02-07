package com.example.LibraryApplication.service;

import com.example.LibraryApplication.model.BorrowedBooks;
import com.example.LibraryApplication.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${app.borrowPeriod}")
    private String borrowPeriod;

    @Async
    public void sendReminderEmailAsync(BorrowedBooks borrow) {
        User user = borrow.getUser();
        String email = user.getEmail();
        String subject = "Overdue Book Reminder";
        String body = "Dear " + user.getName() + ",\n\n" +
                "The book '" + borrow.getBook().getTitle() + "' is overdue. It was due on " + borrow.getDueDate(borrow.getBorrowDate(), Integer.parseInt(borrowPeriod)) +
                ". Please return it as soon as possible.\n\n" +
                "Thank you!";

        sendEmail(email, subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }

}
