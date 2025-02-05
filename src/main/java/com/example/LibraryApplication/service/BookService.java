package com.example.LibraryApplication.service;

import com.example.LibraryApplication.model.Book;
import com.example.LibraryApplication.model.User;
import com.example.LibraryApplication.repository.BookRepository;
import com.example.LibraryApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookService {
//    private final Map<Long, Book> books = new HashMap<>();

    @Autowired
    BookRepository bookRepo;

    UserService userService;

    public List<Book> getAllBooks(){
        return bookRepo.findAll();
    }

    public Book getBookById(int id){
        return bookRepo.findById(id);
    }

    public List<Book> getBookByTitle(String title){
        return bookRepo.findByTitleIgnoreCase(title);
    }

    public List<Book> getBookByAuthor(String author){
        return bookRepo.findByAuthorIgnoreCase(author);
    }

    public List<Book> getBookByIsbn(String isbn){
        return bookRepo.findByIsbn(isbn);
    }

    public Book addBook(Book book){
        book.setAvailable(true);
        return bookRepo.save(book);
    }

    public Book updateBook(int id, Book updatedBook){
        if(bookRepo.findById(id) != null){
            return bookRepo.save(updatedBook);
        }
        return null;
    }

    public String borrowBook(int bookId, int userId){
        Book book = bookRepo.findById(bookId);
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
        Book book = bookRepo.findById(bookId);
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

    public String deleteBook(int bookId, int userId){
        User user = userService.getUserById(userId);
        if(user.getRole().equals("Admin")){
            if(bookRepo.findById(bookId) != null){
                bookRepo.deleteById(bookId);
                return "Book deleted by Admin";
            }
            else{
                return "Book not Found";
            }

        }
        return "User doesn't have the right to delete the book";
    }
}
