package com.example.librarymanagement.service;

import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.repository.BookRepository;
import com.example.librarymanagement.repository.BorrowRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepo;
    private final BorrowRecordRepository borrowRepo;

    public BookService(BookRepository bookRepo,
                       BorrowRecordRepository borrowRepo) {
        this.bookRepo = bookRepo;
        this.borrowRepo = borrowRepo;
    }

    public Book add(Book b) {
        return bookRepo.save(b);
    }

    public List<Book> findAll() {
        return bookRepo.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepo.findById(id);
    }

    @Transactional
    public void delete(Long id) {
        if (!bookRepo.existsById(id)) {
            throw new NoSuchElementException("Book with id " + id + " not found");
        }
        // 1) remove all borrow records referencing this book
        borrowRepo.deleteByBookId(id);
        // 2) delete the book itself
        bookRepo.deleteById(id);
    }
}
