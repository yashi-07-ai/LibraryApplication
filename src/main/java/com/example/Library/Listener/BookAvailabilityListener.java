package com.example.Library.Listener;

import com.example.Library.service.BookAvailabilityEvent;
import com.example.Library.service.BookService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class BookAvailabilityListener {

    private final BookService bookService;

    public BookAvailabilityListener(BookService bookService) {
        this.bookService = bookService;

        // Start listening for book availability updates
        listenToBookAvailabilityUpdates();
    }

    private void listenToBookAvailabilityUpdates() {
        Flux<BookAvailabilityEvent> availabilityStream = bookService.getBookAvailabilityStream();

        // Subscribe to the availability stream to handle updates in real-time
        availabilityStream.subscribe(event -> {
            // Handle the book availability update
            System.out.println("Book: " + event.getBookTitle() + " is " +
                    (event.isAvailable() ? "available" : "not available"));
            // You can implement further logic, like notifying users or updating a UI
        });
    }
}
