package com.example.LibraryApplication.repository;

import com.example.LibraryApplication.model.Book;
import com.example.LibraryApplication.model.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);

    Optional<User> findByEmail(@Email String email);

    @Query("SELECT b FROM User b WHERE b.name ILIKE %:keyword%")
    Optional<List<User>> findByName(@Param("keyword") String keyword);

}
