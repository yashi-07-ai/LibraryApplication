package com.example.Library.service;

import com.example.Library.dto.BookDTO;
import com.example.Library.dto.BookResponseDTO;
import com.example.Library.exception.BookAlreadyExists;
import com.example.Library.exception.NoSuchBookExistsException;
import com.example.Library.exception.NoSuchUserExistsException;
import com.example.Library.model.Book;
import com.example.Library.model.User;
import com.example.Library.repository.BookRepository;
import com.example.Library.repository.UserRepository;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExecutorService executorService;

    private final AtomicInteger successCount = new AtomicInteger(0); // Counter for successful uploads
    private final AtomicInteger failureCount = new AtomicInteger(0); // Counter for failed uploads

    private BookResponseDTO convertToDTO(Book book) {
        BookResponseDTO bookResponseDTO = new BookResponseDTO();
        bookResponseDTO.setId(book.getId());
        bookResponseDTO.setTitle(book.getTitle());
        bookResponseDTO.setAuthor(book.getAuthor());
        bookResponseDTO.setIsbn(book.getIsbn());
        bookResponseDTO.setAvailable(true);
        return bookResponseDTO;
    }

    public Book convertToBook(BookDTO bookDto) {
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setIsbn(bookDto.getIsbn());
        book.setAvailable(true);
        return book;
    }

    private Book convertStringToBook(String line) {
        String[] data = line.split(",");
        Book book = new Book();
        book.setTitle(data[0]);
        book.setAuthor(data[1]);
        book.setIsbn(data[2]);

        return book;
    }

    private static Logger log = LoggerFactory.getLogger(BookService.class);

    public List<BookResponseDTO> getAllBooks(){
        log.info("Getting all the books in the library");
        List<Book> books = bookRepository.findAll();
        if(books.isEmpty()){
            log.error("No book found in library");
            throw new NoSuchBookExistsException("No book found");
        }

        log.info("Displaying the books");
        return books.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public BookResponseDTO getBookById(Long id) {
        log.info("Searching for book with id {}", id);
        Book book =  bookRepository.findById(id).orElseThrow(()-> new NoSuchBookExistsException(
                "No book present with ID = " + id
        ));

        return convertToDTO(book);
    }

    public List<BookResponseDTO> getBookByTitle(String title) {
        log.info("Searching for book with keyword : {}", title);
        List<Book> books = bookRepository.findByTitle(title).orElseThrow(() -> new NoSuchBookExistsException(
                "No book found with title : " + title
        ));

        return books.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookResponseDTO addBook(BookDTO bookDTO, Long id) {
        log.info("Adding a book to library");

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchUserExistsException("User not found"));

        if(user.getRole() != User.UserRole.ADMIN){
            log.error("User don't have the access");
            throw new IllegalArgumentException("User cannot add the book");
        }

        Book book = convertToBook(bookDTO);
//        if (bookDTO.getId() != null) {
//            log.error("Book already present");
//            throw new IllegalArgumentException("ID should be null for new books.");
//        }

        Optional<Book> existingBook = bookRepository.findByIsbn(book.getIsbn()); // Assuming isbn is unique
        if (existingBook.isPresent()) {
            log.error("Book already present");
            throw new BookAlreadyExists("Book already exists");
        }

        // Save the new book
        log.info("Saving the book");
        bookRepository.save(book);
        return convertToDTO(book);
    }

    @Async
    public void processFile(MultipartFile file, Long userId) {
        log.info("Uploading file: {}, Size: {} bytes", file.getOriginalFilename(), file.getSize());

        User user = userRepository.findById(userId).orElseThrow(()->
                new NoSuchUserExistsException("User Not Found")
        );

        if(user.getRole() != User.UserRole.ADMIN){
            throw new IllegalArgumentException("User cannot add books");
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            List<String> lines = br.lines().toList();
            List<Future<?>> futures = new ArrayList<>();

            // Skip the header line and process each line in a separate thread
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                futures.add(executorService.submit(() -> saveBookToDatabase(line)));
            }

            // Wait for all threads to complete
            for (Future<?> future : futures) {
                future.get(); // Block until the task is complete
            }

            // Log the counts
            log.info("File processing completed: {}", file.getOriginalFilename());
            log.info("Total books successfully uploaded: {}", successCount.get());
            log.info("Total books failed to upload: {}", failureCount.get());

        } catch (Exception e) {
            log.error("Error processing file: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveBookToDatabase(String line) {
        try {
            Book book = convertStringToBook(line);
            bookRepository.save(book);
            successCount.incrementAndGet(); // Increment success counter
            log.info("Saved book: {}", book.getTitle());
        } catch (Exception e) {
            failureCount.incrementAndGet(); // Increment failure counter
            log.error("Error saving book: {}", e.getMessage());
        }
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdown(); // Gracefully shutdown the thread pool
        log.info("ExecutorService shutdown complete.");
    }

    public BookResponseDTO updateBook(Long id, BookDTO updatedBook) {
        log.info("Updating the book with id : {}", bookRepository.findById(id));

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchUserExistsException("User not found"));

        if(user.getRole() != User.UserRole.ADMIN){
            log.error("User don't have the access");
            throw new IllegalArgumentException("User cannot updated the book");
        }

        Book book = bookRepository.findByIsbn(updatedBook.getIsbn()).orElseThrow(
                ()-> new NoSuchBookExistsException("Book not found")
        );
        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        book.setIsbn(updatedBook.getIsbn());
        book.setAvailable(updatedBook.isAvailable());

        log.info("saving the updated info");
        bookRepository.save(book);
        log.info("updated details saved");
        return convertToDTO(book);
    }

    public void deleteBook(Long bookId, Long userId){
        // Fetch the user attempting to delete the book
        log.info("fetching book with id : {} to delete", bookId );
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchUserExistsException("User not found"));

        // Check if the user is an ADMIN
        if (user.getRole() != User.UserRole.ADMIN) {
            log.error("User with id {} role is Member", userId);
            throw new IllegalArgumentException("Only ADMIN users can delete books");
        }

        // Fetch the book to delete
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchBookExistsException("Book not found"));

        // Delete the book
        bookRepository.delete(book);
        log.info("Book with id {} deleted", bookId);
    }

}
