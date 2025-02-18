package com.example.Library.service;

public class BookAvailabilityEvent {
    private String bookTitle;
    private boolean isAvailable;

    public BookAvailabilityEvent(String bookTitle, boolean isAvailable) {
        this.bookTitle = bookTitle;
        this.isAvailable = isAvailable;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}

