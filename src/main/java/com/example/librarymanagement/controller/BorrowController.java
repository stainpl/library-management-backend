package com.example.librarymanagement.controller;

import com.example.librarymanagement.model.BorrowRecord;
import com.example.librarymanagement.service.BorrowService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    private final BorrowService svc;

    public BorrowController(BorrowService svc) {
        this.svc = svc;
    }

    @PostMapping
    public BorrowRecord borrow(@RequestParam Long userId,
                               @RequestParam Long bookId) {
        return svc.borrow(userId, bookId);
    }

    @PostMapping("/return")
    public BorrowRecord returnBook(@RequestParam Long bookId) {
        return svc.returnBook(bookId);
    }

    @GetMapping("/active")
    public List<BorrowRecord> getActiveBorrows() {
        return svc.getActiveBorrows();
    }
}
