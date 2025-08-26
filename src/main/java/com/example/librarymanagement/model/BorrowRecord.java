package com.example.librarymanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "borrow_records")
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    /** The book borrowed */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Book book;

    /** When the book was borrowed */
    @Column(nullable = false)
    private LocalDateTime borrowedAt;

    private LocalDateTime returnedAt;

    public BorrowRecord() {
        // JPA default constructor
    }

    public BorrowRecord(User user, Book book) {
        this.user = user;
        this.book = book;
        this.borrowedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDateTime getBorrowedAt() {
        return borrowedAt;
    }
    public void setBorrowedAt(LocalDateTime borrowedAt) {
        this.borrowedAt = borrowedAt;
    }

    public LocalDateTime getReturnedAt() {
        return returnedAt;
    }
    public void setReturnedAt(LocalDateTime returnedAt) {
        this.returnedAt = returnedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BorrowRecord)) return false;
        BorrowRecord that = (BorrowRecord) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BorrowRecord{" +
               "id=" + id +
               ", user=" + (user != null ? user.getId() : null) +
               ", book=" + (book != null ? book.getId() : null) +
               ", borrowedAt=" + borrowedAt +
               ", returnedAt=" + returnedAt +
               '}';
    }
}
