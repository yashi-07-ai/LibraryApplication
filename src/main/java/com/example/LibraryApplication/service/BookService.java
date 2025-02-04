package com.example.LibraryApplication.service;

import com.example.LibraryApplication.model.Book;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookService {
    private final Map<Integer, Book> books = new HashMap<>();
    private int bookIdCounter = 1;

    public List<Book> getAllBooks(){
        return new ArrayList<>(books.values());
    }

    public Book getBookById(int id){
        return books.get(id);
    }

    public Book addBook(Book book){
        book.setId(bookIdCounter++);
        book.setAvailable(true);
        books.put(book.getId(), book);
        return book;
    }

    public Book updateBook(int id, Book updatedBook){
        if(books.containsKey(id)){
            updatedBook.setId(id);
            books.put(id, updatedBook);
            return updatedBook;
        }
        return null;
    }

    public String borrowBook(int bookId, int userId){
        Book book = books.get(bookId);
        if(book == null){
            return "Book not found";
        }
        if(!book.isAvailable()){
            return "Book borrowed by some other user";
        }
        book.setAvailable(false);
        book.setBorrowedBy(userId);

        return "Book borrowed successfully by User : " + userId;
    }

    public String returnBook(int bookId){
        Book book = books.get(bookId);
        if(book == null){
            return "There doesn't exist such book!";
        }
        if(book.isAvailable()){
            return "Book was not borrowed";
        }
        book.setAvailable(true);
        book.setBorrowedBy(null);

        return "Book returned successfully";
    }

    public boolean deleteBook(int id){
        return books.remove(id) != null;
    }
}
