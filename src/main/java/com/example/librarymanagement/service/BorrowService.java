package com.example.librarymanagement.service;

import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.model.BorrowRecord;
import com.example.librarymanagement.model.User;
import com.example.librarymanagement.repository.BookRepository;
import com.example.librarymanagement.repository.BorrowRecordRepository;
import com.example.librarymanagement.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowService {

    private final UserRepository userRepo;
    private final BookRepository bookRepo;
    private final BorrowRecordRepository borrowRepo;

    public BorrowService(UserRepository userRepo,
                         BookRepository bookRepo,
                         BorrowRecordRepository borrowRepo) {
        this.userRepo = userRepo;
        this.bookRepo = bookRepo;
        this.borrowRepo = borrowRepo;
    }

    @Transactional
    public BorrowRecord borrow(Long userId, Long bookId) {
        User u = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Book b = bookRepo.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found"));
        if (!b.isAvailable()) throw new RuntimeException("Book is already borrowed");

        b.setAvailable(false);
        bookRepo.save(b);

        BorrowRecord rec = new BorrowRecord();
        rec.setUser(u);
        rec.setBook(b);
        rec.setBorrowedAt(LocalDateTime.now());

        return borrowRepo.save(rec);
    }

    @Transactional
    public BorrowRecord returnBook(Long bookId) {
        BorrowRecord rec = borrowRepo.findByBookIdAndReturnedAtIsNull(bookId)
            .orElseThrow(() -> new RuntimeException("Active borrow not found"));

        rec.setReturnedAt(LocalDateTime.now());
        Book b = rec.getBook();
        b.setAvailable(true);
        bookRepo.save(b);

        return borrowRepo.save(rec);
    }

    public List<BorrowRecord> getActiveBorrows() {
        return borrowRepo.findByReturnedAtIsNull();
    }
}
