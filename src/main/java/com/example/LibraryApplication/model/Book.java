package com.example.LibraryApplication.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Books")
public class Book {
    private Long id;

    @Column(name = "Title")
    private String title;

    @Column(name = "Author")
    private String author;

    @Column(name = "ISBN")
    private String isbn;

    @Column(name = "Status")
    private boolean available;

    @Column(name = "BorrowedBy")
    private Integer borrowedBy;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookId")
    public Long getId(){
        return id;
    }

    public void setId(long id) {
    }

    public void setAvailable(boolean b) {
        available = b;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setBorrowedBy(Integer userId) {
        borrowedBy = userId;
    }
}
